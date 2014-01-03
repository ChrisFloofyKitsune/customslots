package chrisclark13.minecraft.customslots.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;

public final class GuiHelper {
	
	public static float zLevel;
	
	public static void drawHorizontalLine(float x1, float x2, float y, int color)
    {
        if (x2 < x1)
        {
            float temp = x1;
            x1 = x2;
            x2 = temp;
        }

        drawRect(x1, y, x2 , y + 1, color);
    }

    public static void drawVerticalLine(float x, float y1, float y2, int color)
    {
        if (y2 < y1)
        {
            float temp = y1;
            y1 = y2;
            y2 = temp;
        }

        drawRect(x, y1, x + 1, y2, color);
    }
	
	/**
     * Draws a solid color rectangle with the specified coordinates and color. Args: x1, y1, x2, y2, color
     */
    public static void drawRect(double x1, double y1, double x2, double y2, int color)
    {
        double temp;

        if (x1 < x2)
        {
            temp = x1;
            x1 = x2;
            x2 = temp;
        }

        if (y1 < y2)
        {
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        float f = (float)(color >> 24 & 255) / 255.0F;
        float f1 = (float)(color >> 16 & 255) / 255.0F;
        float f2 = (float)(color >> 8 & 255) / 255.0F;
        float f3 = (float)(color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(f1, f2, f3, f);
        tessellator.startDrawingQuads();
        tessellator.addVertex(x1, y2, 0.0D);
        tessellator.addVertex(x2, y2, 0.0D);
        tessellator.addVertex(x2, y1, 0.0D);
        tessellator.addVertex(x1, y1, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    /**
     * Draws a rectangle with a vertical gradient between the specified colors.
     */
    public static void drawGradientRect(float x1, float y1, float x2, float y2, int color1, int color2)
    {
        float f = (float)(color1 >> 24 & 255) / 255.0F;
        float f1 = (float)(color1 >> 16 & 255) / 255.0F;
        float f2 = (float)(color1 >> 8 & 255) / 255.0F;
        float f3 = (float)(color1 & 255) / 255.0F;
        float f4 = (float)(color2 >> 24 & 255) / 255.0F;
        float f5 = (float)(color2 >> 16 & 255) / 255.0F;
        float f6 = (float)(color2 >> 8 & 255) / 255.0F;
        float f7 = (float)(color2 & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(x2, y1, zLevel);
        tessellator.addVertex(x1, y1, zLevel);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(x1, y2, zLevel);
        tessellator.addVertex(x2, y2, zLevel);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
    public static int packColorFrom3Floats(float[] colors) {
    	int color = 0xFF000000;
    	color |= (int) (colors[0] * 255) << 16;
    	color |= (int) (colors[1] * 255) << 8;
    	color |= (int) (colors[2] * 255);
    	return color;
    }

	public static float[] unpackColorsFromInt(int color) {
		float[] colors = new float[4];
		colors[0] = ((color & 0xFF000000) >>> 24) / 255f;
		colors[1] = ((color & 0x00FF0000) >>> 16) / 255f;
		colors[2] = ((color & 0x0000FF00) >>> 8) / 255f;
		colors[3] = (color & 0x000000FF) / 255f;
		return colors;
	}
    
    
}
