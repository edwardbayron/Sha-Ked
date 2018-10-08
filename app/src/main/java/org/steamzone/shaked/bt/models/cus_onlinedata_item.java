package org.steamzone.shaked.bt.models;

public class cus_onlinedata_item
{

    public int ico;
    public String String1_text;
    public String String1_val;

    public String String2_text;
    public String String2_val;

    public cus_onlinedata_item(int _ico, String String1_text, String String1_val, String String2_text, String String2_val)
    {
        ico = _ico;
        this.String1_text = String1_text;
        this.String1_val = String1_val;

        this.String2_text = String2_text;
        this.String2_val = String2_val;
    }
}