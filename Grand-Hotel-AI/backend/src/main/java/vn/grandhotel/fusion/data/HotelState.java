package vn.grandhotel.fusion.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "hotel_state")
public class HotelState {
    @Id
    private Long id;
    @Column(columnDefinition = "CLOB")
    private String payload;

    protected HotelState() {}
    public HotelState(Long id, String payload) { this.id = id; this.payload = payload; }
    public Long getId() { return id; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
}
