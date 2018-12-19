package game.prototype.objects;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D.Float;
import java.util.LinkedList;

import game.prototype.Game;
import game.prototype.Handler;
import game.prototype.framework.GameObject;
import game.prototype.framework.Helper;
import game.prototype.framework.ObjectId;
import game.prototype.framework.TextureManager;

public class TrailFx extends GameObject {
	private Handler handler;
	private float alpha = 1;
	private float length;
	
	private TextureManager tex = Game.getTexInstance();
	private AffineTransform at;

	public TrailFx(float x, float y, float w, float h, float length, Handler handler,
	               ObjectId id) 
	{
		super(x, y, w, h, id);
		this.handler = handler;
		this.length = length;
	}

	public void update(LinkedList<GameObject> object) {
	    if (alpha > length)
	        alpha -= length -0.0001;	
	    else 
	        handler.removeObject(this);
	    at = AffineTransform.getTranslateInstance(x, y);
	    at.rotate(Math.toRadians(Helper.angle(handler.player.get(0).points()[1],
	                                          handler.player.get(0).points()[0])-90));
	    at.translate(-(w/2), -(h/2));
	    at.scale((w/tex.player[0].getWidth()), (h/tex.player[0].getHeight()));
	    
	    
	}

	public void render(Graphics2D g2) {
	    g2.setComposite(makeTransparent(alpha));
	    g2.drawImage(tex.player[0], at, null);
	    g2.setComposite(makeTransparent(1));	
	}

	private AlphaComposite makeTransparent(float alpha) {
	    int type = AlphaComposite.SRC_OVER;
	    return (AlphaComposite.getInstance(type, alpha));
	}
	
	public Float deltaPoints() {return null;}
	public boolean collision() {return false;}
	public String type() {return null;}
}
