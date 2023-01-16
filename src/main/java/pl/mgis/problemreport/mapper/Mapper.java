package pl.mgis.problemreport.mapper;

import pl.mgis.problemreport.model.*;
import pl.mgis.problemreport.model.dto.*;
import pl.mgis.problemreport.model.User;

public class Mapper {

    public static ReportDTO reportToDto(Report report) {
        ReportDTO reportDTO = new ReportDTO();
        reportDTO.setUuid(report.getUuid());
        reportDTO.setId(report.getId());
        reportDTO.setMessage(report.getMessage());
        reportDTO.setLat(report.getLat());
        reportDTO.setLon(report.getLon());
        reportDTO.setEmail(report.getEmail());
        reportDTO.setPhotoPath(report.getPhotoPath());
        reportDTO.setReportStatusDTO(Mapper.reportStatusToDto(report.getReportStatus()));
        reportDTO.setCreated(report.getAudit().getCreated());
        reportDTO.setReportTypeDTO(Mapper.reportTypeToDto(report.getReportType()));
        return reportDTO;
    }

    public static ReportGjFeature reportToGeoJson(Report report){
        ReportGjFeature reportGjFeature = new ReportGjFeature();
        reportGjFeature.setId(report.getId());
        reportGjFeature.setLat(report.getLat());
        reportGjFeature.setLon(report.getLon());
        reportGjFeature.setCreated(report.getAudit().getCreated());
        reportGjFeature.setStatus(report.getReportStatus().getStatusCode());
        reportGjFeature.setReportStatus(report.getReportStatus().getDescription());
        reportGjFeature.setReportType(report.getReportType().getDescription());
        reportGjFeature.setColor(report.getReportStatus().getColor());
        reportGjFeature.setStage(report.getReportStatus().getStage());
        return reportGjFeature;
    }

    public static ReportWithCommentsDTO reportWithCommentsToDTO(Report report) {
        ReportWithCommentsDTO reportWithCommentsDTO = new ReportWithCommentsDTO();
        reportWithCommentsDTO.setId(report.getId());
        reportWithCommentsDTO.setMessage(report.getMessage());
        reportWithCommentsDTO.setLat(report.getLat());
        reportWithCommentsDTO.setLon(report.getLon());
        reportWithCommentsDTO.setEmail(report.getEmail());
        reportWithCommentsDTO.setPhotoPath(report.getPhotoPath());
        reportWithCommentsDTO.setReportStatusDTO(Mapper.reportStatusToDto(report.getReportStatus()));
        reportWithCommentsDTO.setCreated(report.getAudit().getCreated());
        reportWithCommentsDTO.setComments(null);
        reportWithCommentsDTO.setreportTypeDTO(Mapper.reportTypeToDto(report.getReportType()));
        return reportWithCommentsDTO;
    }

    public static Report reportFromDto(ReportDTO reportDTO) {
        Report report = new Report();
        report.setLat(reportDTO.getLat());
        report.setLon(reportDTO.getLon());
        report.setEmail(reportDTO.getEmail());
        report.setMessage(reportDTO.getMessage());
        return report;
    }

    public static Report reportFromAddDto(ReportSimpleDTO reportSimpleDTO) {
        Report report = new Report();
        report.setLat(reportSimpleDTO.getLat());
        report.setLon(reportSimpleDTO.getLon());
        report.setEmail(reportSimpleDTO.getEmail());
        report.setMessage(reportSimpleDTO.getMessage());
        return report;
    }

    public static CommentDTO commentToDto(Comment comment){
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setContent(comment.getContent());
        commentDTO.setReportId(comment.getReportId());
        commentDTO.setCreated(comment.getAudit().getCreated());
        commentDTO.setUpdated(comment.getAudit().getCreated());
        commentDTO.setSendByEmail(comment.isSendByEmail());
        return commentDTO;
    }

    public static Comment commentFromDto(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setReportId(commentDTO.getReportId());
        comment.setContent(commentDTO.getContent());
        comment.setSendByEmail(commentDTO.isSendByEmail());
        return comment;
    }

    public static ReportTypeDTO reportTypeToDto(ReportType reportType) {
        ReportTypeDTO reportTypeDTO = new ReportTypeDTO();
        reportTypeDTO.setId(reportType.getId());
        reportTypeDTO.setActive(reportType.isActive());
        reportTypeDTO.setDescription(reportType.getDescription());
        reportTypeDTO.setEmail(reportType.getEmail());
        return reportTypeDTO;
    }

    public static ReportType reportTypeFromDto(ReportTypeDTO reportTypeDTO){
        ReportType reportType = new ReportType();
        reportType.setActive(reportTypeDTO.isActive());
        reportType.setDescription(reportTypeDTO.getDescription());
        reportType.setEmail(reportTypeDTO.getEmail());
        return reportType;
    }

    public static ReportStatusDTO reportStatusToDto(ReportStatus reportStatus){
        ReportStatusDTO reportStatusDto = new ReportStatusDTO();
        reportStatusDto.setId(reportStatus.getId());
        reportStatusDto.setStatusCode(reportStatus.getStatusCode());
        reportStatusDto.setDescription(reportStatus.getDescription());
        reportStatusDto.setColor(reportStatus.getColor());
        reportStatusDto.setStage(reportStatus.getStage());
        return reportStatusDto;
    }

    public static ReportStatus reportStatusFromDto(ReportStatusDTO reportStatusDTO) {
        ReportStatus reportStatus = new ReportStatus();
        reportStatus.setStatusCode(reportStatusDTO.getStatusCode());
        reportStatus.setDescription(reportStatusDTO.getDescription());
        reportStatus.setColor(reportStatusDTO.getColor());
        reportStatus.setStage(reportStatusDTO.getStage());
        return reportStatus;
    }

    public static UserDTO userToDto(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserRole(user.getUserRole());
        userDTO.setEnabled(user.isEnabled());
        return userDTO;
    }

    public static UserDTO userToDto(User user, boolean passwordReturn) {
        UserDTO userDTO = userToDto(user);
        if (!passwordReturn)
            userDTO.setPassword(null);
        return userDTO;
    }

    public static User userFromDto(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setUserRole(userDTO.getUserRole());
        user.setEnabled(userDTO.isEnabled());
        return user;
    }
}
