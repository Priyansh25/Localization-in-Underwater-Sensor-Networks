import org.arl.fjage.*
import org.arl.fjage.Message
import org.arl.fjage.RealTimePlatform
import org.arl.unet.phy.*
import org.arl.unet.mac.*
import org.arl.unet.*
import org.arl.unet.net.Router
import org.arl.unet.nodeinfo.NodeInfo
import org.arl.unet.localization.*
import org.arl.unet.localization.RangeNtf.*
import org.arl.unet.localization.Ranging.*
import org.arl.unet.localization.RangeReq

import org.arl.unet.net.RouteDiscoveryReq


class node_agent extends UnetAgent {
    int addr;
    float neighbor_distance;

    void startup() 
    {

      def phy = agentForService Services.PHYSICAL;
      subscribe topic(phy);

      def ranging = agentForService Services.RANGING;
      subscribe topic(ranging);

      def nodeInfo = agentForService Services.NODE_INFO;
      addr = nodeInfo.address;

      def rdp=agentForService Services.ROUTE_MAINTENANCE;
      subscribe topic(rdp);
       
       //ranging<< new RangeReq(to: 104);
       
        int attempts = 1      // try only a single attempt at discovery
        int phantom = 132     // non-existent node address
        int timeout = 10000   // 10 second timeout

        println 'Starting discovery...'
        rdp << new RouteDiscoveryReq(to: phantom, count: attempts)
    
    }
      
    void processMessage(Message msg) {

    if (msg instanceof RangeNtf )
    {   
        float neighbor_distance = msg.getRange();
        
        println " Distance between node "+addr + " and neighbor is " + neighbor_distance;
    }
    else if(msg instanceof RouteDiscoveryNtf)
    {
        def n = []  ;
        println("  Discovered neighbor: ${msg.nextHop}");
        n << msg.nextHop;
    }

   }  
}
