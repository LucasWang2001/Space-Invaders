class Rect{
    private double x,y,h,w;

    public double getX(){
		return x;
    }
    public double getY(){
		return y;
    }
    public double getWidth(){
		return w;
    }
    public double getHeight(){
		return h;
    }

    public Rect(double x, double y, double h, double w){
		this.x = x;
		this.y = y;
		this.h = h;
		this.w = w;
    }

    public void translate(double dx, double dy){
		x += dx;
		y += dy;
    }

    public void resize(double a){
		x -= (w*a-w)/2;
		y -= (h*a-h)/2;
		h *= a;
		w *= a;
    }

    public boolean overlaps(Rect r){
		return x+w >= r.x && r.x + r.w >= x && y+h >= r.y && r.y + r.h >= y;
    }

    public boolean contains(double xx, double yy){
		return xx > x && xx < x + w && yy > y && yy < y + h;
    }

    public double[] center(){
		double []c = new double[2];
		c[0] = x + w/2;
		c[1] = y + h/2;
		return c;
    }
}