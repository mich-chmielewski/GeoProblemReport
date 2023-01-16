package pl.mgis.problemreport.controller.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mgis.problemreport.model.Comment;
import pl.mgis.problemreport.model.dto.CommentDTO;
import pl.mgis.problemreport.service.CommentService;
import pl.mgis.problemreport.service.EmailService;

import java.util.List;

@RestController
@RequestMapping(value = "/manager/api/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable long id) {
        return new ResponseEntity<>(commentService.get(id), HttpStatus.OK);
    }

    @GetMapping(value = "/all")
    public List<Comment> getCommentAll() {
        return commentService.getAll();
    }

    //TODO change req mapping name
    @GetMapping(value = "/report/{report}")
    public List<CommentDTO> getListByReportId(@PathVariable(name = "report") long reportId) {
        return commentService.getAllByReportId(reportId);
    }

    @PostMapping
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.save(commentDTO), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<CommentDTO> editComment(@RequestBody CommentDTO commentDTO) {
        return new ResponseEntity<>(commentService.edit(commentDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable long id) {
        commentService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
