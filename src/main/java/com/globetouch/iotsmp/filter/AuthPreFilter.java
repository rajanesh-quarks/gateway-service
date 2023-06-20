package com.globetouch.iotsmp.filter;

import io.micrometer.core.instrument.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
@Slf4j
public class AuthPreFilter implements GlobalFilter {

	public final static String CLIENT_AUTH_TOKEN = "authorization";
	public final static String AUTH_TOKEN = "request_token";

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("filter is invoked...");
		HttpHeaders header = exchange.getRequest().getHeaders();
		if(header.get(CLIENT_AUTH_TOKEN) == null){
			throw new RuntimeException("Authorization token missing ");
		}
		String token = header.get(CLIENT_AUTH_TOKEN).get(0);
		log.info("filter is invoked..."+ token);
		boolean authorizationStatus = validateToken(token);
		//boolean authorizationStatus = true;
		if(authorizationStatus){
			return chain.filter(exchange);
		}
		return Mono.error(new Throwable("Not Authorized Successfully."));
	}

	private static boolean validateToken(String token) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("https://192.168.1.37:8020/api/v2/token/verification");
		httpGet.addHeader(AUTH_TOKEN, token);
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
            if (statusLine == null || statusLine.getStatusCode() != 200) {
				log.error("Authorization failed, ", statusLine.getStatusCode());
				return false;
            }
			String msg = IOUtils.toString(response.getEntity().getContent());
			System.out.println("response: "+msg);
		} catch (Exception e) {
			log.error("Authorization failed, ", e.getMessage());
			e.printStackTrace();
			return false;
		}finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
		}
		return true;
	}
}
