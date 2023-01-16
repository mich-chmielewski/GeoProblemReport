package pl.mgis.problemreport.controller.api;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.mgis.problemreport.model.Stage;
import pl.mgis.problemreport.model.dto.*;
import pl.mgis.problemreport.service.CommentService;
import pl.mgis.problemreport.service.ReportService;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/manager/api/report")
public class ReportController {

    private final ReportService reportService;
    private final CommentService commentService;

    public ReportController(ReportService reportService, CommentService commentService) {
        this.reportService = reportService;
        this.commentService = commentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReportDTO> getReport(@Valid @PathVariable long id) {
        return new ResponseEntity<>(reportService.get(id), HttpStatus.OK);
    }

    /**
     * Special controller for handling ajax request from jQuery DataTable plugin.
     */

    @PostMapping("/datatable")
    public ResponseEntity<ReportDataTableDTO> getReportDataForDataTable(@RequestParam Map<String, String> allParams) {

        int start = Integer.parseInt(extractedFromParams(allParams, "start"));
        int length = Integer.parseInt(extractedFromParams(allParams, "length"));
        int draw = Integer.parseInt(extractedFromParams(allParams, "draw"));
        String order = extractedFromParams(allParams, "order[0][dir]");
        int sortColIndex = Integer.parseInt(extractedFromParams(allParams, "order[0][column]"));
        String sortColName = extractedFromParams(allParams, "columns[" + sortColIndex + "][data]");
        String searchText = extractedFromParams(allParams, "search[value]");
        return new ResponseEntity<>(reportService.dataTableReports(start, length, draw, order, sortColName,
                searchText), HttpStatus.OK);
    }

    private String extractedFromParams(Map<String, String> params, String key) {
        return params.entrySet().stream().filter(k -> k.getKey().equals(key))
                .findFirst().get().getValue();
    }

    @GetMapping("/list-paged")
    public ResponseEntity<List<ReportDTO>> getReportAllPaged(Pageable page) {
        return new ResponseEntity<>(reportService.getAllPaged(page), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ReportDTO>> getReportAll(@PathParam("page") int page, @PathParam("size") int size, @PathParam("sort") Sort.Direction sort) {
        Sort.Direction sortDirection = sort != null ? sort : Sort.Direction.ASC;
        return new ResponseEntity<>(reportService.getAll(getRequestPage(page), getRequestSize(size), sort), HttpStatus.OK);
    }

    @GetMapping("/list-comments")
    public ResponseEntity<List<ReportWithCommentsDTO>> getReportAllComments(@PathParam("page") int page, @PathParam("size") int size) {
        return new ResponseEntity<>(reportService.getAllWithComments(PageRequest.of(getRequestPage(page), getRequestSize(size))), HttpStatus.OK);
    }

    @CrossOrigin
    @PostMapping("/")
    public ResponseEntity<ReportDTO> addReport(@Valid @RequestBody ReportSimpleDTO reportSimpleDTO) {
        return new ResponseEntity<>(reportService.add(reportSimpleDTO), HttpStatus.OK);
    }

    @PutMapping("/")
    public ResponseEntity<ReportDTO> edit(@Valid @RequestBody ReportSimpleDTO reportSimpleDTO) {
        return new ResponseEntity<>(reportService.editReport(reportSimpleDTO), HttpStatus.ACCEPTED);
    }

    @PutMapping("/{id}/{statusCode}")
    public ResponseEntity<HttpStatus> changeStatus(@PathVariable long id, @PathVariable int statusCode) {
        reportService.changeStatus(id, statusCode);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteReport(@Valid @PathVariable long id) {
        reportService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin
    @GetMapping("/geojson")
    public ResponseEntity<ReportGeoJson> getGeoJson() {
        ReportGeoJson reportGeoJson = new ReportGeoJson();
        reportGeoJson.setFeatures(reportService.getAllGjFeature());
        return new ResponseEntity<>(reportGeoJson, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/geojson/active")
    public ResponseEntity<ReportGeoJson> getGeoJsonActive() {
        ReportGeoJson reportGeoJson = new ReportGeoJson();
        reportGeoJson.setFeatures(reportService.getAllGjFeature().stream().filter(r->r.getStage() != Stage.FINISHED ).collect(Collectors.toList()));
        return new ResponseEntity<>(reportGeoJson, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/geojson/finished")
    public ResponseEntity<ReportGeoJson> getGeoJsonFinished() {
        ReportGeoJson reportGeoJson = new ReportGeoJson();
        reportGeoJson.setFeatures(reportService.getAllGjFeature().stream().filter(r->r.getStage() == Stage.FINISHED).collect(Collectors.toList()));
        return new ResponseEntity<>(reportGeoJson, HttpStatus.OK);
    }

    private int getRequestPage(int page) {
        return Math.max(page, 0);
    }

    private int getRequestSize(int size) {
        return size < 1 || size > 50 ? 50 : size;
    }

    @CrossOrigin
    @GetMapping("/typechartdata")
    public ResponseEntity<?> getChartDataType(){
        List<Map<String, Integer>> reportsStatistic = reportService.getReportsStatistic();
        Set<Map.Entry<String, Integer>> typeData = reportsStatistic.get(0).entrySet();
        return new ResponseEntity<>(typeData, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/statuschartdata")
    public ResponseEntity<?> getChartDataStatus(){
        List<Map<String, Integer>> reportsStatistic = reportService.getReportsStatistic();
        Set<Map.Entry<String, Integer>> statusData = reportsStatistic.get(1).entrySet();
        return new ResponseEntity<>(statusData, HttpStatus.OK);
    }
}
