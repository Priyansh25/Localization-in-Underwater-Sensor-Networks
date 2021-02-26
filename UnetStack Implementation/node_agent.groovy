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

class node_agent extends UnetAgent {
    float neighbor_addr;
    float distance;
    
    def xlist = [];
    def ylist = [];
    def zlist = [];

    def dlist = [];
    def adlist = [];
    
  private final static PDU format = PDU.withFormat
  {
    uint16('source')
  }

  void startup() 
  {
    def phy = agentForService Services.PHYSICAL;
    subscribe topic(phy);

    println 'Starting discovery...'
    phy << new DatagramReq(to: 0, protocol:Protocol.MAC);
  }
    
  void processMessage(Message msg) 
  {
    def ranging = agentForService Services.RANGING;
    subscribe topic(ranging);

    if(msg instanceof RxFrameNtf && msg.protocol==Protocol.MAC)
    {
       def rx = format.decode(msg.data);
       neighbor_addr=rx.source;
       
       println "Found one anchor with address "+neighbor_addr;
       
       adlist.add(neighbor_addr)
       println adlist;
       if(adlist.size()==3)     //As soon as I get the address of 3 neighbour
       {                        //nodes query the range and coordinates information.
           
           println "Found 3 anchor nodes, getting locations....";
           println ' '
           
           def i=0;
           float n1=adlist[0];
           float n2=adlist[1];
            float n3=adlist[2];
           
           
              //waker behavior to avoid collision in RangeNtf
              
                  
                  add new WakerBehavior(1000,{
                  ranging<< new RangeReq(to: n1,requestLocation: true)})
                  
                  add new WakerBehavior(15000,{
                  ranging<< new RangeReq(to: n2,requestLocation: true)})
                  
                  add new WakerBehavior(30000,{
                  ranging<< new RangeReq(to: n3,requestLocation: true)})
                  
              
       }
    }
    else if (msg instanceof RangeNtf )
    {   
       distance = msg.getRange();
       def locat=new double[3];
       locat = msg.getPeerLocation();
        
       double x,y,z;
       x=locat[0];
       y=locat[1];
       z=locat[2];
       
       xlist.add(x);
       ylist.add(y);
       zlist.add(z);
       
       dlist.add(distance);

       println " The coordinates of "+msg.to + " are " +locat+ " and the distance is "+distance;
       
       if(dlist.size()==3)
       {
            println ' '
            println 'x-coordinates'+ xlist;
            println 'y-coordinates'+ ylist;
            println 'z-coordinates'+ zlist;
            println 'ranges'+ dlist;
          
         
    BigDecimal X, Y, Va, Vb, Xa, Xb, Xc, Ya, Yb, Yc, Da, Db, Dc, tmp1, tmp2, tmp3, tmp4, tmp5;

    Xa = xlist[0]; 			// Initializing the parameters
    Ya = ylist[0];
    Xb = xlist[1]; 
    Yb = ylist[1];
    Xc = xlist[2];  
    Yc = ylist[2];
    Da = dlist[0];   
    Db = dlist[1];
    Dc = dlist[2];
    
    tmp1=(Math.pow(Xb, 2) - Math.pow(Xa, 2))	
    tmp2=(Math.pow(Yb, 2) - Math.pow(Ya, 2))
    tmp3=(Math.pow(Db, 2) - Math.pow(Da, 2))	// tmp1, tmp2, tmp3 are intermediate variables to avoid overflow

    Va = ( tmp1 + tmp2 - tmp3) / 2;
    
    tmp1=(Math.pow(Xb, 2) - Math.pow(Xc, 2))
    tmp2=(Math.pow(Yb, 2) - Math.pow(Yc, 2))
    tmp3=(Math.pow(Db, 2) - Math.pow(Dc, 2))	// tmp1, tmp2, tmp3 are intermediate variables to avoid overflow
    
    Vb = (tmp1 + tmp2 - tmp3) / 2;
    
    tmp4=(Va * (Yb - Yc) - Vb * (Yb - Ya))
    tmp5=((Yb - Yc) * (Xb - Xa) - (Yb - Ya) * (Xb - Xc))	// tmp4, tmp5 are intermediate variables to avoid overflow

    X = tmp4 / tmp5;
    
    tmp4=(Va * (Xb - Xc) - Vb * (Xb - Xa))
    tmp5=((Yb - Ya) * (Xb - Xc) - (Xb - Xa) * (Yb - Yc))	// tmp4, tmp5 are intermediate variables to avoid overflow
    
    Y = tmp4 / tmp5

    X = Math.round(X * 100000) / 100000			
    Y = Math.round(Y * 100000) / 100000		// precision of upto 5 digits after decimal
    
    println ' '
    println "The coordinates of the Blind node are "
    println "("+ X + " , " +  Y + ")" 

       }
    }
  }
}
