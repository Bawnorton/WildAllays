package com.bawnorton.wildallays.util;

public class Colour {
    private float r, g, b, a;
    private float h, s, v;
    public Colour() {
        this(1, 1, 1);
    }

    public Colour(int r, int g, int b) {
        this(r / 255F, g / 255F, b / 255F);
    }

    public Colour(float r, float g, float b) {
        this(r, g, b, 1);
    }

    public Colour(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        setHSV();
    }

    public static Colour fromBinary(int binary) {
        int red = (binary >> 16) & 0xff;
        int green = (binary >> 8) & 0xff;
        int blue = binary & 0xff;
        return new Colour(red / 255F, green / 255F, blue / 255F);
    }

    public void adjustTo(Colour toColour, int delta) {
        int deltaR = (int) Math.min(delta, 255 * Math.abs(this.r() - toColour.r()));
        int deltaG = (int) Math.min(delta, 255 * Math.abs(this.g() - toColour.g()));
        int deltaB = (int) Math.min(delta, 255 * Math.abs(this.b() - toColour.b()));

        if(this.r() < toColour.r()) this.shiftR(deltaR);
        else if(this.r() > toColour.r()) this.shiftR(-deltaR);
        if(this.g() < toColour.g()) this.shiftG(deltaG);
        else if(this.g() > toColour.g()) this.shiftG(-deltaG);
        if(this.b() < toColour.b()) this.shiftB(deltaB);
        else if(this.b() > toColour.b()) this.shiftB(-deltaB);
    }

    private void setHSV() {
        float cmax = Math.max(r, Math.max(g, b));
        float cmin = Math.min(r, Math.min(g, b));
        float diff = cmax - cmin;
        if (cmax == cmin) h = 0;
        else if (cmax == r) h = (60 * ((g - b) / diff) + 360) % 360 / 360;
        else if (cmax == g) h = (60 * ((b - r) / diff) + 120) % 360 / 360;
        else if (cmax == b) h = (60 * ((r - g) / diff) + 240) % 360 / 360;
        if (cmax == 0) s = 0;
        else s = (diff / cmax);
        v = cmax;
    }

    public void setRGB() {
        int j = (int)(h * 6);
        float f = h * 6 - j;
        float p = v * (1 - s);
        float q = v * (1 - f * s);
        float t = v * (1 - (1 - f) * s);

        switch (j) {
            case 0 -> rgbHelper(v, t, p);
            case 1 -> rgbHelper(q, v, p);
            case 2 -> rgbHelper(p, v, t);
            case 3 -> rgbHelper(p, q, v);
            case 4 -> rgbHelper(t, p, v);
            case 5 -> rgbHelper(v, p, q);
        }
    }

    public void rgbHelper(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    private static float wrap(float num) {
        while(num > 1F) num -= 1F;
        while(num < 0F) num += 1F;
        return num;
    }

    private static float bound(float num) {
        return Math.min(Math.max(num, 0F), 1F);
    }

    public void shiftH(int amount) {
        h = wrap(h + amount / 360F);
        setRGB();
    }

    public void shiftS(int amount) {
        s = wrap(s + amount / 100F);
        setRGB();
    }

    public void shiftV(int amount) {
        v = wrap(v + amount / 100F);
        setRGB();
    }

    public void shiftR(int amount) {
        r = wrap(r + amount / 255F);
        setHSV();
    }

    public void shiftG(int amount) {
        g = wrap(g + amount / 255F);
        setHSV();
    }

    public void shiftB(int amount) {
        b = wrap(b + amount / 255F);
        setHSV();
    }

    public void shiftA(int amount) {
        a = wrap(a + amount / 255F);
        setHSV();
    }

    public void setH(int amount) {
        h = bound(amount / 360F);
        setRGB();
    }

    public void setS(int amount) {
        s = bound(amount / 100F);
        setRGB();
    }

    public void setV(int amount) {
        v = bound(amount / 100F);
        setRGB();
    }

    public void setR(int amount) {
        r = bound(amount / 255F);
        setHSV();
    }

    public void setG(int amount) {
        g = bound(amount / 255F);
        setHSV();
    }

    public void setB(int amount) {
        b = bound(amount / 255F);
        setHSV();
    }

    public void setA(int amount) {
        a = bound(amount / 255F);
        setHSV();
    }

    public float[] hsv() {
        return new float[]{h, s, v};
    }

    public float[] rgb() {
        return new float[]{r, g, b};
    }

    public float[] rgba() {
        return new float[]{r, g, b, a};
    }

    public float h() {
        return h;
    }

    public float s() {
        return s;
    }

    public float v() {
        return v;
    }

    public float r() {
        return r;
    }

    public float g() {
        return g;
    }

    public float b() {
        return b;
    }

    public float a() {
        return a;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Colour colour) {
            return colour.r() == this.r() &&
                    colour.g() == this.g() &&
                    colour.b() == this.b() &&
                    colour.a() == this.a();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Colour: [RGBA(" +
                "" + (int) (r * 255) +
                ", " + (int) (g * 255) +
                ", " + (int) (b * 255) +
                ", " + (int) (a * 255) +
                "), HSV(" +
                "" + (int) (h * 360) +
                ", " + (int) (s * 100) +
                ", " + (int) (v * 100) +
                ")]";
    }
}
