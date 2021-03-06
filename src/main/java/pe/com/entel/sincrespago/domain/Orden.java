package pe.com.entel.sincrespago.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Erika Rumiche on 28/09/2018.
 */
public class Orden {

    private Long ordenId;
    private Long clienteCrmId;
    private Long siteOvepId;
    private Long ovepId;
    private List<String> simnumberList;

    public Orden(Long ordenId, Long clienteCrmId, Long siteOvepId, Long ovepId) {
        this.ordenId = ordenId;
        this.clienteCrmId = clienteCrmId;
        this.siteOvepId = siteOvepId;
        this.ovepId = ovepId;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Long ordenId) {
        this.ordenId = ordenId;
    }

    public Long getClienteCrmId() {
        return clienteCrmId;
    }

    public void setClienteCrmId(Long clienteCrmId) {
        this.clienteCrmId = clienteCrmId;
    }

    public Long getSiteOvepId() {
        return siteOvepId;
    }

    public void setSiteOvepId(Long siteOvepId) {
        this.siteOvepId = siteOvepId;
    }

    public List<String> getSimnumberList() {
        if(simnumberList == null){
            simnumberList = new ArrayList<String>();
        }
        return simnumberList;
    }

    public Long getOvepId() {
        return ovepId;
    }

    public void setOvepId(Long ovepId) {
        this.ovepId = ovepId;
    }

    @Override
    public String toString() {
        return "Orden{" +
                "ordenId=" + ordenId +
                ", clienteCrmId=" + clienteCrmId +
                ", siteOvepId=" + siteOvepId +
                ", ovepId=" + ovepId +
                ", simnumberList=" + simnumberList +
                '}';
    }
}
