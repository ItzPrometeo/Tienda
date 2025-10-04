package service;

import com.paypal.core.PayPalEnvironment;
import com.paypal.core.PayPalHttpClient;
import com.paypal.orders.*;

import config.PayPalConfig;

import java.io.IOException;
import java.util.List;

public class PayPalService {
    private final PayPalHttpClient client;

    public PayPalService() {
        PayPalEnvironment environment = new PayPalEnvironment.Sandbox(
            PayPalConfig.CLIENT_ID,
            PayPalConfig.SECRET
        );  
        client = new PayPalHttpClient(environment);
    }

    public Order createOrder(double total, String returnUrl, String cancelUrl) throws IOException {
    OrdersCreateRequest request = new OrdersCreateRequest();
    request.prefer("return=representation");
    request.requestBody(
        new OrderRequest()
            .checkoutPaymentIntent("CAPTURE")
            .applicationContext(new ApplicationContext()
                .returnUrl(returnUrl)     // 🔁 Redirección correcta
                .cancelUrl(cancelUrl)
                .landingPage("BILLING")   // O "LOGIN"
                .userAction("PAY_NOW")
            )
            .purchaseUnits(List.of(
                new PurchaseUnitRequest()
                    .amountWithBreakdown(new AmountWithBreakdown()
                        .currencyCode("USD")
                        .value(String.format("%.2f", total)))
            ))
    );

    return client.execute(request).result();
}

    public Order captureOrder(String orderId) throws IOException {
        OrdersCaptureRequest request = new OrdersCaptureRequest(orderId);
        return client.execute(request).result();
    }
}
