package org.springframework.cloud.netflix.feign.ribbon;

import com.netflix.client.ClientException;
import com.netflix.client.config.IClientConfig;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import java.io.IOException;
import java.net.URI;

public class ContextAwareLoadBalancerFeignClient extends LoadBalancerFeignClient {

   private final Client delegate;
   private CachingSpringLoadBalancerFactory lbClientFactory;

   public ContextAwareLoadBalancerFeignClient(Client delegate, CachingSpringLoadBalancerFactory lbClientFactory, SpringClientFactory clientFactory) {
      super(delegate, lbClientFactory, clientFactory);
      this.delegate = delegate;
      this.lbClientFactory = lbClientFactory;
   }

   @Override
   public Response execute(Request request, Request.Options options) throws IOException {
      try {
         URI asUri = URI.create(request.url());

         String clientName = asUri.getHost();

         FeignLoadBalancer feignLoadBalancer = this.lbClientFactory.create(clientName);

         DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) feignLoadBalancer.getLoadBalancer().chooseServer(null);

         String homePageUrl = discoveryEnabledServer.getInstanceInfo().getHomePageUrl();

         String serviceContext = URI.create(homePageUrl).getPath();

         URI uriWithoutHost = URI.create(request.url().replaceFirst(clientName, serviceContext));

         FeignLoadBalancer.RibbonRequest ribbonRequest = new FeignLoadBalancer.RibbonRequest(
                 this.delegate, request, uriWithoutHost);

         IClientConfig requestConfig = getClientConfig(options, clientName);

         return feignLoadBalancer.executeWithLoadBalancer(ribbonRequest, requestConfig).toResponse();
      }
      catch (ClientException e) {
         IOException io = findIOException(e);
         if (io != null) {
            throw io;
         }
         throw new RuntimeException(e);
      }
   }
}