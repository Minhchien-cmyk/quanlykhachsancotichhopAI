package vn.grandhotel.fusion.web;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import vn.grandhotel.fusion.data.HotelState;
import vn.grandhotel.fusion.data.HotelStateRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class HotelDataController {
    private static final Set<String> ALLOWED_KEYS = Set.of(
        "grandhotel_rooms", "grandhotel_service_logs", "grandhotel_service_catalog",
        "grandhotel_revenue_history", "grandhotel_booking_requests", "grandhotel_service_requests",
        "grandhotel_admin_support_requests", "grandhotel_payment_requests",
        "grandhotel_system_activities", "grandhotel_customer_feedbacks"
    );
    private final HotelStateRepository repository;
    private final ObjectMapper mapper;

    public HotelDataController(HotelStateRepository repository, ObjectMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @GetMapping("/health")
    public Map<String, String> health() { return Map.of("status", "ok", "service", "Grand Hotel AI Fusion"); }

    @GetMapping("/hotel-data")
    public Map<String, Object> getHotelData() {
        return repository.findById(1L).map(row -> {
            try { return mapper.readValue(row.getPayload(), new TypeReference<LinkedHashMap<String, Object>>() {}); }
            catch (Exception e) { throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không đọc được dữ liệu khách sạn", e); }
        }).orElseGet(LinkedHashMap::new);
    }

    @PutMapping("/hotel-data")
    public Map<String, Object> saveHotelData(@RequestBody Map<String, Object> submitted) {
        Map<String, Object> safe = new LinkedHashMap<>();
        submitted.forEach((key, value) -> { if (ALLOWED_KEYS.contains(key)) safe.put(key, value); });
        try {
            String json = mapper.writeValueAsString(safe);
            if (json.length() > 2_000_000) throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "Dữ liệu vượt giới hạn 2 MB");
            repository.save(repository.findById(1L).map(row -> { row.setPayload(json); return row; }).orElseGet(() -> new HotelState(1L, json)));
            return Map.of("saved", true, "keys", safe.size());
        } catch (ResponseStatusException e) { throw e; }
        catch (Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dữ liệu gửi lên không hợp lệ", e); }
    }
}
