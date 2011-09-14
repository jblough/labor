package com.josephblough.laborstatistics.data;

public class Tip {

    public String title;
    public String filename;
    public int resource;
    
    public Tip(final String title, final String filename, final int resource) {
	this.title = title;
	this.filename = filename;
	this.resource = resource;
    }
    
    @Override
    public String toString() {
        return title;
    }
}
