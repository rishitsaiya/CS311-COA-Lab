package processor.memorysystem;

import generic.*;
import processor.*;
import configuration.Configuration;

public class Cache implements Element {
	Processor containingProcessor;
	public int latency ;
	int csize ;
	int miss_addr;
	int read;
    int write_data;
    CacheLine[] cach;
    boolean isPresent = true;
    int temp;
    int[] index;
	public Cache(Processor containingProcessor, int l, int s) {
		this.containingProcessor = containingProcessor;
        this.latency = l;
        this.csize = s;
        temp   = (int)(Math.log(this.csize/8)/Math.log(2));
        cach=new CacheLine[csize/8];
		for(int i=0;i<csize/8;i++) {
			cach[i]=new CacheLine();
		}
    }
    
    public void handleCacheMiss(int addr ) {
		Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
						Clock.getCurrentTime()+Configuration.mainMemoryLatency,
                        this,
                        containingProcessor.getMainMemory(),
                        addr));
                        

		
	}

    
    public int cacheRead(int address){
        String a=Integer.toBinaryString(address);
        String ind ="";
        int temp_ind;
       
        for(int i =0;i<32-a.length();i++){
            a = "0" + a;
        }
        for(int i = 0; i<temp;i++ ){
            ind = ind + "1";
        }
        if(temp ==0){
            temp_ind = 0;
        }
        else
          {
               temp_ind = address & Integer.parseInt(ind, 2);
          }
        System.out.println("in the Cache " +address);
        int add_tag = Integer.parseInt(a.substring(0, a.length()-temp),2);
        if(add_tag ==cach[temp_ind].tag[0]){
            cach[temp_ind].lru = 1;
            isPresent = true;
            return cach[temp_ind].data[0];
        }
        else if(add_tag ==cach[temp_ind].tag[1]){
            cach[temp_ind].lru = 0;
            isPresent = true;
            return cach[temp_ind].data[1];
        }
        else {
            isPresent = false;
            return -1;

        }

    }

    public void cacheWrite(int address, int value){
        String a=Integer.toBinaryString(address);
        String ind ="";
        int temp_ind;

        for(int i =0;i<32-a.length();i++){
            a = "0" + a;
        }
        for(int i = 0; i<temp;i++ ){
            ind = ind + "1";
        }
        if(temp ==0){
            temp_ind = 0;
        }
        else
          {
               temp_ind = address & Integer.parseInt(ind, 2);
          }
        // int temp_ind = address & Integer.parseInt(ind, 2);
        int tag = Integer.parseInt(a.substring(0, a.length()-temp),2);
        cach[temp_ind].setValue(tag, value);


    }


    @Override
	public void handleEvent(Event e) {
        if(e.getEventType() == Event.EventType.MemoryRead){
            System.out.println("handle event cache memory read");
            MemoryReadEvent ee = (MemoryReadEvent) e;
            int data = cacheRead(ee.getAddressToReadFrom());
            if(isPresent == true){
                Simulator.getEventQueue().addEvent(
                    new MemoryResponseEvent(
                        Clock.getCurrentTime() + this.latency, 
                        this, 
                        ee.getRequestingElement(), 
                        data)
                );
            }
            else{
                System.out.println("Missed");
                this.miss_addr = ee.getAddressToReadFrom();
                ee.setEventTime(Clock.getCurrentTime()+Configuration.mainMemoryLatency+1);
                Simulator.getEventQueue().addEvent(ee);
                handleCacheMiss(ee.getAddressToReadFrom());
            }
        }
       else if(e.getEventType() == Event.EventType.MemoryResponse){
            MemoryResponseEvent ee = (MemoryResponseEvent) e;
            cacheWrite(this.miss_addr, ee.getValue());
            // Simulator.getEventQueue().addEvent(
            //     new MemoryResponseEvent(
            //         Clock.getCurrentTime() + 1, 
            //         this, 
            //         ee.getRequestingElement(), 
            //         data)
            // );
        }
        
        else if(e.getEventType() == Event.EventType.MemoryWrite){
            System.out.println("handle event cache memory write");
            MemoryWriteEvent ee = (MemoryWriteEvent) e;
            cacheWrite(ee.getAddressToWriteTo(), ee.getValue());
            // int data = cacheRead(ee.getAddressToWriteTo());
            containingProcessor.getMainMemory().setWord(ee.getAddressToWriteTo(), ee.getValue());
            // if(true) {
            //     cacheWrite(ee.getAddressToWriteTo(), ee.getValue());
            // }
            Simulator.getEventQueue().addEvent(
				new ExecutionCompleteEvent(
					Clock.getCurrentTime()+Configuration.mainMemoryLatency, 
					containingProcessor.getMainMemory(), 
					ee.getRequestingElement())
			);
            // cacheWrite(this.miss_addr, ee.getValue());
            // Simulator.getEventQueue().addEvent(
            //     new MemoryResponseEvent(
            //         Clock.getCurrentTime() + 1, 
            //         this, 
            //         ee.getRequestingElement(), 
            //         data)
            // );
		}
		
		
	}

}