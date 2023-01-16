package pl.mgis.problemreport.model.dto;

import java.util.List;

public class ReportDataTableDTO {

    public int draw;
    public int recordsTotal;
    public int recordsFiltered;

    public List<ReportDTO> data;

    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getRecordsTotal() {
        return recordsTotal;
    }

    public void setRecordsTotal(int recordsTotal) {
        this.recordsTotal = recordsTotal;
    }

    public int getRecordsFiltered() {
        return recordsFiltered;
    }

    public void setRecordsFiltered(int recordsFiltered) {
        this.recordsFiltered = recordsFiltered;
    }

    public List<ReportDTO> getData() {
        return data;
    }

    public void setData(List<ReportDTO> data) {
        this.data = data;
    }
}
