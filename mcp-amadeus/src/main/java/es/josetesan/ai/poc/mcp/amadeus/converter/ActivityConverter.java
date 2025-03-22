package es.josetesan.ai.poc.mcp.amadeus.converter;

import com.amadeus.resources.Activity;
import es.josetesan.ai.poc.mcp.amadeus.model.ShopActivity;
import org.springframework.stereotype.Service;

@Service
public class ActivityConverter {

    public ShopActivity convert(Activity activity) {
        return new ShopActivity(
                activity.getName(),
                activity.getShortDescription(),
                activity.getRating(),
                activity.getMinimumDuration(),
                activity.getPrice().getAmount(),
                activity.getPrice().getCurrencyCode()
        );
    }
}
