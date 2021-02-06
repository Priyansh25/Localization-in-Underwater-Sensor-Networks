import org.arl.fjage.Message
import org.arl.unet.PDU
import org.arl.unet.*
import org.arl.unet.net.Router
import org.arl.unet.nodeinfo.NodeInfo

class MyAgent extends UnetAgent {
    def loc;
    def addr;
    
    private final static PDU format = PDU.withFormat
    {
        uint16('source')
        uint16('coordinates')
    }
 
  void startup() {
    
    phy = agentForService Services.PHYSICAL    //to communicate between two nodes
    subscribe topic(phy)
    
    def node = agentForService Services.NODE_INFO;
    addr = node.address;
    loc  = node.location;

    def datapacket = format.encode(source: addr, coordinates: loc)
    
  }
  
  void processMessage(Message msg) {
    
    if(msg instanceof RouteDiscoveryNtf)
    {
       phy << new TxFrameReq(to: Address.BROADCAST, protocol: cluster_protocol, data: datapacket)
    }
  }

}