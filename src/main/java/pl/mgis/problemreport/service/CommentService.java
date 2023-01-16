package pl.mgis.problemreport.service;

import org.springframework.stereotype.Service;
import pl.mgis.problemreport.exception.ResourceNotFoundException;
import pl.mgis.problemreport.mapper.Mapper;
import pl.mgis.problemreport.model.Comment;
import pl.mgis.problemreport.model.dto.CommentDTO;
import pl.mgis.problemreport.repository.CommentRepository;
import pl.mgis.problemreport.repository.ReportRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReportRepository reportRepository;
    private final EmailService emailService;

    public CommentService(CommentRepository commentRepository, ReportRepository repository, EmailService emailService) {
        this.commentRepository = commentRepository;
        this.reportRepository = repository;
        this.emailService = emailService;
    }

    public CommentDTO get(long id) {
        return Mapper.commentToDto(commentRepository.findById(id).orElseThrow());
    }

    public CommentDTO save(CommentDTO commentDTO) {
        CommentDTO verifiedCommentDTO = verifyAndSendEmailNotification(commentDTO);
        return Mapper.commentToDto(commentRepository.save(Mapper.commentFromDto(verifiedCommentDTO)));
    }

    private CommentDTO verifyAndSendEmailNotification(CommentDTO commentDTO) {
        if (commentDTO.isSendByEmail()) {
            reportRepository.findById(commentDTO.getReportId())
                    .ifPresent(r -> {
                        if (r.getEmail() != null && !r.getEmail().isEmpty()) {
                            emailService.sendInformationReportMessage(Mapper.reportToDto(r), commentDTO);
                            return;
                        }
                        commentDTO.setSendByEmail(false);
                        }
                    );
        }
        return commentDTO;
    }

    public List<Comment> getAll() {
        return commentRepository.findAll();
    }

    public List<CommentDTO> getAllByReportId(long reportId) {
        return commentRepository.findListUsingReportId(reportId).stream()
                .map(Mapper::commentToDto)
                .collect(Collectors.toList());
    }

    public void delete(long id) {
        commentRepository.deleteById(id);
    }

    public CommentDTO edit(CommentDTO commentDTO) {
        Comment comment  = commentRepository.findById(commentDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found"));
        comment.setContent(commentDTO.getContent());
        CommentDTO verifiedCommentDTO = verifyAndSendEmailNotification(Mapper.commentToDto(comment));
        comment.setSendByEmail(verifiedCommentDTO.isSendByEmail());
        return Mapper.commentToDto(commentRepository.save(comment));
    }
}
