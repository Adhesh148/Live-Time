package com.vaadin.timetable.backend;

import com.vaadin.flow.component.grid.ItemClickEvent;

public class TableEntry {

   private String SLOT_I = "";
   private String SLOT_II = "";
   private String SLOT_III = "";
   private String SLOT_IV = "";
   private String SLOT_V = "";
   private String SLOT_VI = "";
   private String SLOT_VII = "";
   private String SLOT_VIII = "";
   private String SLOT_IX = "";

   public String getSLOT_I(){
       return SLOT_I;
   }

    public String getSLOT_II() {
       return SLOT_II;
   }

    public String getSLOT_III() {
        return SLOT_III;
    }

    public String getSLOT_IV() {
        return SLOT_IV;
    }

    public String getSLOT_V() {
        return SLOT_V;
    }

    public String getSLOT_VI() {
        return SLOT_VI;
    }

    public String getSLOT_VII() {
        return SLOT_VII;
    }

    public String getSLOT_VIII() {
        return SLOT_VIII;
    }

    public String getSLOT_IX() {
        return SLOT_IX;
    }

    public void setSLOT_I(String SLOT_I) {
        this.SLOT_I = SLOT_I;
    }

    public void setSLOT_II(String SLOT_II) {
        this.SLOT_II = SLOT_II;
    }

    public void setSLOT_III(String SLOT_III) {
        this.SLOT_III = SLOT_III;
    }

    public void setSLOT_IV(String SLOT_IV) {
        this.SLOT_IV = SLOT_IV;
    }

    public void setSLOT_V(String SLOT_V) {
        this.SLOT_V = SLOT_V;
    }

    public void setSLOT_VI(String SLOT_VI) {
        this.SLOT_VI = SLOT_VI;
    }

    public void setSLOT_VII(String SLOT_VII) {
        this.SLOT_VII = SLOT_VII;
    }

    public void setSLOT_VIII(String SLOT_VIII) {
        this.SLOT_VIII = SLOT_VIII;
    }

    public void setSLOT_IX(String SLOT_IX) {
        this.SLOT_IX = SLOT_IX;
    }


    public String getSlot(ItemClickEvent<TableEntry> evt, String slot) {
       if(slot.equals("SLOT_I")){
           return evt.getItem().getSLOT_I();
       }
       else if(slot.equals("SLOT_II")){
           return evt.getItem().getSLOT_II();
       }
       else if(slot.equals("SLOT_III")){
           return evt.getItem().getSLOT_III();
       }
       else if(slot.equals("SLOT_IV")){
           return evt.getItem().getSLOT_IV();
       }
       else if(slot.equals("SLOT_V")){
           return evt.getItem().getSLOT_V();
       }
       else if(slot.equals("SLOT_VI")){
           return evt.getItem().getSLOT_VI();
       }
       else if(slot.equals("SLOT_VII")){
           return evt.getItem().getSLOT_VII();
       }
       else if(slot.equals("SLOT_VIII")){
           return evt.getItem().getSLOT_VIII();
       }
       else {
           return evt.getItem().getSLOT_IX();
       }

    }
}
