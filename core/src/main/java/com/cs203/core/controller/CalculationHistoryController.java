import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cs203.core.service.CalculationHistoryService;

import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Calculation History")
@RestController
@RequestMapping("/api/v1/get-calculations")
public class CalculationHistoryController {
    @Autowired
    CalculationHistoryService calculationHistoryService;

}
