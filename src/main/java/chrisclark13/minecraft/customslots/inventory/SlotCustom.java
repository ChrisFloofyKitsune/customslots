package chrisclark13.minecraft.customslots.inventory;

import org.lwjgl.opengl.GL11;

import chrisclark13.minecraft.customslots.client.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotCustom extends Slot {

	protected static RenderItem itemRenderer = new RenderItem();
	protected static Minecraft mc = Minecraft.getMinecraft();

	/** display position of the inventory slot on the screen x axis */
	public float x;

	/** display position of the inventory slot on the screen y axis */
	public float y;

	public float width;
	public float height;

	public float padding;

	public SlotCustom(IInventory inventory, int slotIndex, float xPosition,
			float yPosition, float width, float height) {
		super(inventory, slotIndex, (int) xPosition, (int) yPosition);

		this.x = xPosition;
		this.y = yPosition;
		this.width = width;
		this.height = height;
		padding = 1;
	}

	/*
	 * CustomSlot drawing code start
	 */

	public void drawBackground() {
		final int DARK_COLOR = 0xFF373737;
		final int MID_COLOR = 0xFF8B8B8B;
		final int LIGHT_COLOR = 0xFFFFFFFF;

		// Draw bg square
		GuiHelper.drawRect(x, y, x + width, y + height, MID_COLOR);

		// Draw shading
		GuiHelper.drawHorizontalLine(x, x + width - 1, y, DARK_COLOR);
		GuiHelper.drawVerticalLine(x, y, y + height - 1, DARK_COLOR);

		// Draw highlights
		GuiHelper.drawHorizontalLine(x + 1, x + width, y + height - 1,
				LIGHT_COLOR);
		GuiHelper.drawVerticalLine(x + width - 1, y + 1, y + height,
				LIGHT_COLOR);
	}

	public void drawInventory() {
		ItemStack stack = this.getStack();

		if (stack != null) {
			drawStackInSlot(stack, null);
		}
	}

	/**
	 * @param stack
	 */
	public void drawStackInSlot(ItemStack stack, String text) {
		if (stack != null) {
			itemRenderer.zLevel = 100.0F;
			FontRenderer font = stack.getItem().getFontRenderer(stack);

			if (font == null)
				font = mc.fontRenderer;

			float xScale = (width - padding - padding) / 16f;
			float yScale = (height - padding - padding) / 16f;

			GL11.glPushMatrix();
			GL11.glTranslatef(x + padding, y + padding, 0);
			GL11.glScalef(xScale, yScale, 1);

			itemRenderer.renderItemAndEffectIntoGUI(font,
					mc.getTextureManager(), stack, 0, 0);
			itemRenderer.renderItemOverlayIntoGUI(font, mc.getTextureManager(),
					stack, 0, 0, text);

			GL11.glPopMatrix();

			itemRenderer.zLevel = 0.0F;
		}
	}

	public void drawSelectionHighlight() {

		final int HIGHLIGHT_COLOR = 0x80FFFFFF;

		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		GuiHelper.zLevel = 100;
		GuiHelper.drawGradientRect(x + padding, y + padding, x + width
				- padding, y + height - padding, HIGHLIGHT_COLOR,
				HIGHLIGHT_COLOR);
		GuiHelper.zLevel = 0;

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

	}

	public boolean isMouseOver(float mouseX, float mouseY) {
		return (mouseX >= x && mouseX < (x + width) && mouseY >= y && mouseY < (y + height));
	}

	public void drawHeldStackOverSlot(ItemStack stack, float mouseX,
			float mouseY) {
		this.drawHeldStackOverSlot(stack, mouseX, mouseY, "");
	}

	public void drawHeldStackOverSlot(ItemStack stack, float mouseX,
			float mouseY, String prefix) {

		if (stack != null) {
			GL11.glTranslatef(0, 0, 32.0F);

			float xScale = (width - padding - padding) / 16f;
			float yScale = (height - padding - padding) / 16f;

			GL11.glPushMatrix();
			GL11.glTranslatef(mouseX, mouseY, 0);
			GL11.glScalef(xScale, yScale, 1);

			itemRenderer.zLevel = 200.0F;

			FontRenderer font = stack.getItem().getFontRenderer(stack);

			if (font == null)
				font = mc.fontRenderer;

			itemRenderer.renderItemAndEffectIntoGUI(font,
					mc.getTextureManager(), stack, -8, -8);
			itemRenderer.renderItemOverlayIntoGUI(font, mc.getTextureManager(),
					stack, -8, -8, prefix);

			itemRenderer.zLevel = 0.0F;

			GL11.glPopMatrix();
		}
	}

}
