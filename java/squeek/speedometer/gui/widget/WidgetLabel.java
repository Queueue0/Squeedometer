package squeek.speedometer.gui.widget;

import net.minecraft.client.renderer.GlStateManager;
import squeek.speedometer.gui.IGuiHierarchical;

public class WidgetLabel extends WidgetBase
{
	protected String text;
	protected int color = 0xa0a0a0; //0x404040;
	protected boolean drawShadow = false;
	public boolean drawCentered = true;

	public WidgetLabel(IGuiHierarchical parent, int x, int y, String text)
	{
		super(parent, x, y);
		this.text = text;
		this.setSize(MC.fontRendererObj.getStringWidth(this.text), MC.fontRendererObj.FONT_HEIGHT);
	}

	public WidgetLabel(IGuiHierarchical parent, int x, int y, String text, int color, boolean drawShadow)
	{
		super(parent, x, y);
		this.text = text;
		this.setSize(MC.fontRendererObj.getStringWidth(this.text), MC.fontRendererObj.FONT_HEIGHT);
		this.color = color;
		this.drawShadow = drawShadow;
	}

	@Override
	public void drawForeground(int mouseX, int mouseY)
	{
		if (this.text != null && !this.text.equals(""))
		{
			int x = this.x;
			int y = this.y;
			if (drawCentered)
			{
				x -= MC.fontRendererObj.getStringWidth(this.text) / 2;
				y -= MC.fontRendererObj.FONT_HEIGHT / 2;
			}
			GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
			MC.fontRendererObj.drawString(this.text, x, y, this.color, this.drawShadow);
		}
		super.drawForeground(mouseX, mouseY);
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public void setColor(int color)
	{
		this.color = color;
	}

	public void setDrawShadow(boolean drawShadow)
	{
		this.drawShadow = drawShadow;
	}

	public String getText()
	{
		return text;
	}

	public int getColor()
	{
		return color;
	}

	public boolean getDrawShadow()
	{
		return drawShadow;
	}
}
