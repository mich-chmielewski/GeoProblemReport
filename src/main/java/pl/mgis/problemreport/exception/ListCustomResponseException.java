package pl.mgis.problemreport.exception;

import java.util.ArrayList;
import java.util.List;

public class ListCustomResponseException extends RuntimeException {

    List<CustomResponseException> customResponseExceptionList;

    public ListCustomResponseException(List<CustomResponseException> customResponseExceptionList) {
        this.customResponseExceptionList = customResponseExceptionList;
    }

    public List<CustomResponseException> getCustomResponseExceptionList() {
        return customResponseExceptionList;
    }

    public void setCustomResponseExceptionList(List<CustomResponseException> customResponseExceptionList) {
        this.customResponseExceptionList = customResponseExceptionList;
    }
}
