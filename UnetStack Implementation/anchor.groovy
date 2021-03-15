import org.arl.fjage.*
import org.arl.fjage.Message
import org.arl.fjage.RealTimePlatform
import org.arl.unet.phy.*
import org.arl.unet.mac.*
import org.arl.unet.*
import org.arl.unet.PDU
import org.arl.unet.net.Router
import org.arl.unet.nodeinfo.NodeInfo
import org.arl.unet.localization.*
import org.arl.unet.localization.RangeNtf.*
import org.arl.unet.localization.Ranging.*
import org.arl.unet.localization.RangeReq
import org.arl.unet.net.RouteDiscoveryReq

class anchor_agent extends UnetAgent {
    def addr;
    def nme;
    
  private final static PDU format = PDU.withFormat
    {
        uint16('source')
    }
  
  void startup() 
  {
    def phy = agentForService Services.PHYSICAL;
    subscribe topic(phy);
  }
  
  void processMessage(Message msg) 
  {
    def phy = agentForService Services.PHYSICAL;
    subscribe topic(phy);
    
    def nodeInfo = agentForService Services.NODE_INFO;
    addr = nodeInfo.address;
    nme = nodeInfo.nodeName;

    def datapacket = format.encode(source: addr);
    
    if(msg instanceof DatagramNtf && msg.protocol==Protocol.MAC)
    { 
       def n=rndint(5);    // n and k are two random numbers generated to increase 
       def k=rndint(6);   // the entropy and therby avoid collisions
                        
       
       println "Broadcast packet received at node "+nme;
       println "Node "+nme+" will respond after "+ (n+1)*(k+1)+" seconds"
       println ' '
        
        // After receiving the broadcast request from the bind node the anchor node will respond after cetain delay 
        // this delay is defined here as ( (n+1)*(k+1)*500 ) miliseconds.
        
       def delay= (n+1)*(k+1)*1000
        
       add new WakerBehavior(delay,{
       phy << new TxFrameReq(to: msg.from, 
                             protocol: Protocol.MAC, 
                             data: datapacket)  
       })
           
     
    }
  }
}
