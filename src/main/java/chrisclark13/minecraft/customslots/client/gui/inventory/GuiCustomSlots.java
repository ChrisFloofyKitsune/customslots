package chrisclark13.minecraft.customslots.client.gui.inventory;

import java.util.Iterator;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import chrisclark13.minecraft.customslots.inventory.ContainerCustomSlots;
import chrisclark13.minecraft.customslots.inventory.SlotCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import static cpw.mods.fml.common.ObfuscationReflectionHelper.*;

public abstract class GuiCustomSlots extends GuiContainer {

	protected ContainerCustomSlots containerCustomSlots;
	protected SlotCustom selectedCustomSlot;

	public GuiCustomSlots(ContainerCustomSlots containerCustomSlots) {
		super(containerCustomSlots);
		this.containerCustomSlots = containerCustomSlots;
		
	}

	/**
	 * Draws the screen and all the components in it.
	 */
	public void drawScreen(int mouseX, int mouseY, float paritialTicks) {
		// Kludge go!
		Slot theSlot = null;
		ItemStack draggedStack = getDraggedStack();
		boolean isRightMouseClick = getIsRightMouseClick();
		int field_94069_F = getField_94069_F();
		ItemStack returningStack = getReturningStack();
		long returningStackTime = getReturningStackTime();
		Slot returningStackDestSlot = getReturningStackDestSlot();
		int field_85049_r = getField_85049_r();
		int field_85048_s = getField_85048_s();

		this.drawDefaultBackground();
		int guiLeft = this.guiLeft;
		int guiTop = this.guiTop;
		this.drawGuiContainerBackgroundLayer(paritialTicks, mouseX, mouseY);

		// Draw CustomSlot Backgrounds
		GL11.glPushMatrix();
		GL11.glTranslatef((float) guiLeft, (float) guiTop, 0.0F);
		for (SlotCustom customSlot : containerCustomSlots.customSlots) {
			if (customSlot.func_111238_b())
				customSlot.drawBackground();
		}
		GL11.glPopMatrix();

		GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		RenderHelper.disableStandardItemLighting();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);

		// Super super call, it's ugly, I don't like it, but screw it.
		for (int index = 0; index < this.buttonList.size(); ++index) {
			GuiButton guibutton = (GuiButton) this.buttonList.get(index);
			guibutton.drawButton(this.mc, mouseX, mouseY);
		}

		RenderHelper.enableGUIStandardItemLighting();
		GL11.glPushMatrix();
		GL11.glTranslatef((float) guiLeft, (float) guiTop, 0.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		this.selectedCustomSlot = null;
		setTheSlot(null); // this.theSlot = null;
		short short1 = 240;
		short short2 = 240;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit,
				(float) short1 / 1.0F, (float) short2 / 1.0F);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		int i1;

		// Draw CustomSlot inventories
		for (SlotCustom customSlot : containerCustomSlots.customSlots) {
			if (customSlot.func_111238_b()) {
				if (this.field_94076_q && this.field_94077_p.size() > 1
						&& this.field_94077_p.contains(customSlot)) {
					ItemStack heldStack = mc.thePlayer.inventory.getItemStack();

					if (heldStack != null) {
						if (Container.func_94527_a(customSlot, heldStack, true)
								&& this.inventorySlots
										.canDragIntoSlot(customSlot)) {
							ItemStack drawStack = heldStack.copy();
							Container.func_94525_a(this.field_94077_p,
									getField_94071_C(), drawStack, customSlot
											.getStack() == null ? 0
											: customSlot.getStack().stackSize);

							String text = null;
							if (drawStack.stackSize > drawStack
									.getMaxStackSize()) {
								text = EnumChatFormatting.YELLOW + ""
										+ drawStack.getMaxStackSize();
								drawStack.stackSize = drawStack
										.getMaxStackSize();
							}

							if (drawStack.stackSize > customSlot
									.getSlotStackLimit()) {
								text = EnumChatFormatting.YELLOW + ""
										+ customSlot.getSlotStackLimit();
								drawStack.stackSize = customSlot
										.getSlotStackLimit();
							}

							customSlot.drawSelectionHighlight();
							customSlot.drawStackInSlot(drawStack, text);
						} else {
							this.field_94077_p.remove(customSlot);
							this.func_94066_g();
						}
					}
				} else {
					customSlot.drawInventory();
				}
			}
		}

		// Draw CustomSlot Selection Highlight & Select a CustomSlot
		for (SlotCustom customSlot : containerCustomSlots.customSlots) {
			float trueMouseX = ((float) Mouse.getX() / mc.displayWidth) * width;
			float trueMouseY = ((float) (mc.displayHeight - Mouse.getY()) / mc.displayHeight)
					* height;

			if (customSlot.func_111238_b()
					&& customSlot.isMouseOver(trueMouseX - guiLeft, trueMouseY
							- guiTop)) {
				this.selectedCustomSlot = customSlot;
				setTheSlot(selectedCustomSlot);
				customSlot.drawSelectionHighlight();
				break;
			}
		}

		// Vanilla slot drawing and selection
		for (int j1 = 0; j1 < this.containerCustomSlots.normalSlots.size(); ++j1) {
			Slot slot = (Slot) this.containerCustomSlots.normalSlots.get(j1);
			this.drawSlotInventory(slot);

			if (this.selectedCustomSlot == null
					&& this.isMouseOverSlot(slot, mouseX, mouseY)
					&& slot.func_111238_b()) {
				theSlot = slot;
				setTheSlot(slot); // this.theSlot = slot;
				GL11.glDisable(GL11.GL_LIGHTING);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				int k1 = slot.xDisplayPosition;
				i1 = slot.yDisplayPosition;
				this.drawGradientRect(k1, i1, k1 + 16, i1 + 16, -2130706433,
						-2130706433);
				GL11.glEnable(GL11.GL_LIGHTING);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
			}
		}

		// Forge: Force lighting to be disabled as there are some issue where
		// lighting would
		// incorrectly be applied based on items that are in the inventory.
		GL11.glDisable(GL11.GL_LIGHTING);
		this.drawGuiContainerForegroundLayer(mouseX, mouseY);
		GL11.glEnable(GL11.GL_LIGHTING);

		InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
		ItemStack itemstack = draggedStack == null ? inventoryplayer
				.getItemStack() : draggedStack;

		if (itemstack != null) {
			byte b0 = 8;
			i1 = draggedStack == null ? 8 : 16;
			String s = null;

			if (draggedStack != null && isRightMouseClick) {
				itemstack = itemstack.copy();
				itemstack.stackSize = MathHelper
						.ceiling_float_int((float) itemstack.stackSize / 2.0F);
			} else if (this.field_94076_q && this.field_94077_p.size() > 1) {
				itemstack = itemstack.copy();
				itemstack.stackSize = field_94069_F;

				if (itemstack.stackSize == 0) {
					s = "" + EnumChatFormatting.YELLOW + "0";
				}
			}

			if (selectedCustomSlot != null) {
				this.selectedCustomSlot.drawHeldStackOverSlot(itemstack, mouseX
						- guiLeft, mouseY - guiTop - (i1 - 8), s);
			} else {
				this.drawItemStack(itemstack, mouseX - guiLeft - b0, mouseY
						- guiTop - i1, s);
			}
		}

		if (returningStack != null) {
			float f1 = (float) (Minecraft.getSystemTime() - returningStackTime) / 100.0F;

			if (f1 >= 1.0F) {
				f1 = 1.0F;
				returningStack = null;
				setReturningStack(null); // this.returningStack = null;
			}

			i1 = returningStackDestSlot.xDisplayPosition - field_85049_r;
			int l1 = returningStackDestSlot.yDisplayPosition - field_85048_s;
			int i2 = field_85049_r + (int) ((float) i1 * f1);
			int j2 = field_85048_s + (int) ((float) l1 * f1);
			this.drawItemStack(returningStack, i2, j2, (String) null);
		}

		GL11.glPopMatrix();

		if (inventoryplayer.getItemStack() == null && theSlot != null
				&& theSlot.getHasStack()) {
			ItemStack itemstack1 = theSlot.getStack();
			this.drawItemStackTooltip(itemstack1, mouseX, mouseY);
		}

		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		RenderHelper.enableStandardItemLighting();
	}

	/*
	 * Copied private parent methods to perpetuate kludge.
	 */

	/**
	 * Returns if the passed mouse position is over the specified slot.
	 * 
	 * awy.a(Lwe;II)Z
	 */
	@Override
	protected boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
		if (slot instanceof SlotCustom) {
			float trueMouseX = ((float) Mouse.getX() / mc.displayWidth) * width;
			float trueMouseY = ((float) (mc.displayHeight - Mouse.getY()) / mc.displayHeight)
					* height;
			return ((SlotCustom) slot).isMouseOver(trueMouseX - guiLeft,
					trueMouseY - guiTop);
		} else {
			return this.isPointInRegion(slot.xDisplayPosition,
					slot.yDisplayPosition, 16, 16, mouseX, mouseY);
		}
	}

	/**
	 * Returns the slot at the given coordinates or null if there is none.
	 * 
	 * awy.c(II)Lwe
	 */
	protected Slot getSlotAtPosition(int posX, int posY) {
		for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k) {
			Slot slot = (Slot) this.inventorySlots.inventorySlots.get(k);

			if (this.isMouseOverSlot(slot, posX, posY)) {
				return slot;
			}
		}

		return null;
	}

	/**
	 * awy.a(Lye;IILjava/lang/String;)V
	 * @param par1ItemStack
	 * @param par2
	 * @param par3
	 * @param par4Str
	 */
	@Override
	protected void drawItemStack(ItemStack par1ItemStack, int par2, int par3,
			String par4Str) {
		GL11.glTranslatef(0.0F, 0.0F, 32.0F);
		this.zLevel = 200.0F;
		itemRenderer.zLevel = 200.0F;
		FontRenderer font = null;
		if (par1ItemStack != null)
			font = par1ItemStack.getItem().getFontRenderer(par1ItemStack);
		if (font == null)
			font = fontRenderer;
		itemRenderer.renderItemAndEffectIntoGUI(font,
				this.mc.getTextureManager(), par1ItemStack, par2, par3);
		itemRenderer.renderItemOverlayIntoGUI(font,
				this.mc.getTextureManager(), par1ItemStack, par2, par3
						- (getDraggedStack() == null ? 0 : 8), par4Str);
		this.zLevel = 0.0F;
		itemRenderer.zLevel = 0.0F;
	}

	@SuppressWarnings("rawtypes")
	/**
	 * awy.g()V
	 */
	protected void func_94066_g() {
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

		if (itemstack != null && this.field_94076_q) {
			setField_94069_F(itemstack.stackSize); // this.field_94069_F =
													// itemstack.stackSize;
			ItemStack itemstack1;
			int i;

			for (Iterator iterator = this.field_94077_p.iterator(); iterator
					.hasNext(); setField_94069_F(getField_94069_F()
					- itemstack1.stackSize - i)) {
				Slot slot = (Slot) iterator.next();
				itemstack1 = itemstack.copy();
				i = slot.getStack() == null ? 0 : slot.getStack().stackSize;
				Container.func_94525_a(this.field_94077_p, getField_94071_C(),
						itemstack1, i);

				if (itemstack1.stackSize > itemstack1.getMaxStackSize()) {
					itemstack1.stackSize = itemstack1.getMaxStackSize();
				}

				if (itemstack1.stackSize > slot.getSlotStackLimit()) {
					itemstack1.stackSize = slot.getSlotStackLimit();
				}
			}
		}
	}

	/*
	 * Copied parent protected methods to make sure that our version of it's
	 * private methods are always called.
	 * 
	 * Honestly it would probably be best to do this with access transformers.
	 */

	/**
	 * Called when the mouse is clicked.
	 */
	protected void mouseClicked(int mouseX, int mouseY, int click) {
		// super.mouseClicked(mouseX, mouseY, click);

		// Another superSuper call! BLARGH.
		if (click == 0) {
			for (int l = 0; l < this.buttonList.size(); ++l) {
				GuiButton guibutton = (GuiButton) this.buttonList.get(l);

				if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
					setSelectedButton(guibutton); // this.selectedButton =
													// guibutton;
					this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
					this.actionPerformed(guibutton);
				}
			}
		}

		boolean flag = click == this.mc.gameSettings.keyBindPickBlock.keyCode + 100;
		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		long l = Minecraft.getSystemTime();
		setField_94074_J(getField_94072_H() == slot
				&& l - getField_94070_G() < 250L && getField_94073_I() == click);
		// this.field_94074_J = this.field_94072_H == slot
		// && l - this.field_94070_G < 250L && this.field_94073_I == par3;
		// this.field_94074_J = this.field_94072_H == slot && l -
		// this.field_94070_G < 250L && this.field_94073_I == par3;
		setField_94068_E(false); // this.field_94068_E = false;

		if (click == 0 || click == 1 || flag) {
			int i1 = this.guiLeft;
			int j1 = this.guiTop;
			boolean flag1 = mouseX < i1 || mouseY < j1
					|| mouseX >= i1 + this.xSize || mouseY >= j1 + this.ySize;
			int k1 = -1;

			if (slot != null) {
				k1 = slot.slotNumber;
			}

			if (flag1) {
				k1 = -999;
			}

			if (this.mc.gameSettings.touchscreen && flag1
					&& this.mc.thePlayer.inventory.getItemStack() == null) {
				this.mc.displayGuiScreen((GuiScreen) null);
				return;
			}

			if (k1 != -1) {
				if (this.mc.gameSettings.touchscreen) {
					if (slot != null && slot.getHasStack()) {
						setClickedSlot(slot); // this.clickedSlot = slot;
						setDraggedStack(null); // this.draggedStack = null;
						setIsRightMouseClick(click == 1); // this.isRightMouseClick
															// = par3 == 1;
					} else {
						setClickedSlot(null); // this.clickedSlot = null;
					}
				} else if (!this.field_94076_q) {
					if (this.mc.thePlayer.inventory.getItemStack() == null) {
						if (click == this.mc.gameSettings.keyBindPickBlock.keyCode + 100) {
							this.handleMouseClick(slot, k1, click, 3);
						} else {
							boolean flag2 = k1 != -999
									&& (Keyboard.isKeyDown(42) || Keyboard
											.isKeyDown(54));
							byte b0 = 0;

							if (flag2) {
								setField_94075_K(slot != null
										&& slot.getHasStack() ? slot.getStack()
										: null);
								// this.field_94075_K = slot != null
								// && slot.getHasStack() ? slot.getStack()
								// : null;
								b0 = 1;
							} else if (k1 == -999) {
								b0 = 4;
							}

							this.handleMouseClick(slot, k1, click, b0);
						}

						setField_94068_E(true); // this.field_94068_E = true;
					} else {
						this.field_94076_q = true;
						setField_94067_D(click); // this.field_94067_D = par3;
						this.field_94077_p.clear();

						if (click == 0) {
							setField_94071_C(0); // this.field_94071_C = 0;
						} else if (click == 1) {
							setField_94071_C(1); // this.field_94071_C = 1;
						}
					}
				}
			}
		}

		setField_94072_H(slot); // this.field_94072_H = slot;
		setField_94070_G(l); // this.field_94070_G = l;
		setField_94073_I(click); // this.field_94073_I = par3;
	}

	/**
	 * Called when a mouse button is pressed and the mouse is moved around.
	 * Parameters are : mouseX, mouseY, lastButtonClicked & timeSinceMouseClick.
	 */
	@SuppressWarnings("unchecked")
	protected void mouseClickMove(int par1, int par2, int par3, long par4) {
		Slot slot = this.getSlotAtPosition(par1, par2);
		ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();

		Slot clickedSlot = getClickedSlot();

		if (clickedSlot != null && this.mc.gameSettings.touchscreen) {
			if (par3 == 0 || par3 == 1) {
				if (getDraggedStack() == null) {
					if (slot != clickedSlot) {
						setDraggedStack(clickedSlot.getStack().copy()); // this.draggedStack
																		// =
																		// this.clickedSlot.getStack().copy();
					}
				} else if (getDraggedStack().stackSize > 1
						&& slot != null
						&& Container.func_94527_a(slot, getDraggedStack(),
								false)) {
					long i1 = Minecraft.getSystemTime();

					if (getField_92033_y() == slot) {
						if (i1 - getField_92032_z() > 500L) {
							this.handleMouseClick(clickedSlot,
									clickedSlot.slotNumber, 0, 0);
							this.handleMouseClick(slot, slot.slotNumber, 1, 0);
							this.handleMouseClick(clickedSlot,
									clickedSlot.slotNumber, 0, 0);
							setField_92032_z(i1 + 750L); // this.field_92032_z =
															// i1 + 750L;
							--getDraggedStack().stackSize;
						}
					} else {
						setField_92033_y(slot); // this.field_92033_y = slot;
						setField_92032_z(i1); // this.field_92032_z = i1;
					}
				}
			}
		} else if (this.field_94076_q && slot != null && itemstack != null
				&& itemstack.stackSize > this.field_94077_p.size()
				&& Container.func_94527_a(slot, itemstack, true)
				&& slot.isItemValid(itemstack)
				&& this.inventorySlots.canDragIntoSlot(slot)) {
			this.field_94077_p.add(slot);
			this.func_94066_g();
		}
	}

	/**
	 * Called when the mouse is moved or a mouse button is released. Signature:
	 * (mouseX, mouseY, which) which==-1 is mouseMove, which==0 or which==1 is
	 * mouseUp
	 */
	@SuppressWarnings("rawtypes")
	protected void mouseMovedOrUp(int mouseX, int mouseY, int which) {
		// Fixing a vanilla derp with some kludge
		if (getSelectedButton() != null && which == 0) {
			getSelectedButton().mouseReleased(mouseX, mouseY);
			setSelectedButton(null); // this.selectedButton = null;
		}

		Slot slot = this.getSlotAtPosition(mouseX, mouseY);
		int l = this.guiLeft;
		int i1 = this.guiTop;
		boolean flag = mouseX < l || mouseY < i1 || mouseX >= l + this.xSize
				|| mouseY >= i1 + this.ySize;
		int j1 = -1;

		if (slot != null) {
			j1 = slot.slotNumber;
		}

		if (flag) {
			j1 = -999;
		}

		Slot slot1;
		Iterator iterator;

		if (getField_94074_J() && slot != null && which == 0
				&& this.inventorySlots.func_94530_a((ItemStack) null, slot)) {
			if (isShiftKeyDown()) {
				if (slot != null && slot.inventory != null
						&& getField_94075_K() != null) {
					iterator = this.inventorySlots.inventorySlots.iterator();

					while (iterator.hasNext()) {
						slot1 = (Slot) iterator.next();

						if (slot1 != null
								&& slot1.canTakeStack(this.mc.thePlayer)
								&& slot1.getHasStack()
								&& slot1.inventory == slot.inventory
								&& Container.func_94527_a(slot1,
										getField_94075_K(), true)) {
							this.handleMouseClick(slot1, slot1.slotNumber,
									which, 1);
						}
					}
				}
			} else {
				this.handleMouseClick(slot, j1, which, 6);
			}

			setField_94074_J(false); // this.field_94074_J = false;
			setField_94070_G(0L); // this.field_94070_G = 0L;
		} else {
			if (this.field_94076_q && getField_94067_D() != which) {
				this.field_94076_q = false;
				this.field_94077_p.clear();
				setField_94068_E(true); // this.field_94068_E = true;
				return;
			}

			if (getField_94068_E()) {
				setField_94068_E(false); // this.field_94068_E = false;
				return;
			}

			boolean flag1;

			Slot clickedSlot = getClickedSlot();
			if (clickedSlot != null && this.mc.gameSettings.touchscreen) {
				if (which == 0 || which == 1) {
					if (getDraggedStack() == null && slot != clickedSlot) {
						setDraggedStack(clickedSlot.getStack()); // this.draggedStack
																	// =
																	// this.clickedSlot.getStack();
					}

					flag1 = Container.func_94527_a(slot, getDraggedStack(),
							false);

					if (j1 != -1 && getDraggedStack() != null && flag1) {
						this.handleMouseClick(clickedSlot,
								clickedSlot.slotNumber, which, 0);
						this.handleMouseClick(slot, j1, 0, 0);

						if (this.mc.thePlayer.inventory.getItemStack() != null) {
							this.handleMouseClick(clickedSlot,
									clickedSlot.slotNumber, which, 0);
							setField_85049_r(mouseX - l); // this.field_85049_r
															// =
															// par1 - l;
							setField_85048_s(mouseY - i1);// this.field_85048_s
															// =
															// par2 - i1;
							setReturningStackDestSlot(clickedSlot); // this.returningStackDestSlot
																	// =
																	// this.clickedSlot;
							setReturningStack(getDraggedStack()); // this.returningStack
																	// =
																	// this.draggedStack;
							setReturningStackTime(Minecraft.getSystemTime()); // this.returningStackTime
																				// =
																				// Minecraft.getSystemTime();
						} else {
							setReturningStack(null); // this.returningStack =
														// null;
						}
					} else if (getDraggedStack() != null) {
						setField_85049_r(mouseX - l); // this.field_85049_r =
														// par1 - l;
						setField_85048_s(mouseY - i1); // this.field_85048_s =
														// par2 - i1;
						setReturningStackDestSlot(clickedSlot); // this.returningStackDestSlot
																// =
																// this.clickedSlot;
						setReturningStack(getDraggedStack()); // this.returningStack
																// =
																// this.draggedStack;
						setReturningStackTime(Minecraft.getSystemTime()); // this.returningStackTime
																			// =
																			// Minecraft.getSystemTime();
					}

					setDraggedStack(null); // this.draggedStack = null;
					setClickedSlot(null); // this.clickedSlot = null;
				}
			} else if (this.field_94076_q && !this.field_94077_p.isEmpty()) {
				this.handleMouseClick((Slot) null, -999,
						Container.func_94534_d(0, getField_94071_C()), 5);
				iterator = this.field_94077_p.iterator();

				while (iterator.hasNext()) {
					slot1 = (Slot) iterator.next();
					this.handleMouseClick(slot1, slot1.slotNumber,
							Container.func_94534_d(1, getField_94071_C()), 5);
				}

				this.handleMouseClick((Slot) null, -999,
						Container.func_94534_d(2, getField_94071_C()), 5);
			} else if (this.mc.thePlayer.inventory.getItemStack() != null) {
				if (which == this.mc.gameSettings.keyBindPickBlock.keyCode + 100) {
					this.handleMouseClick(slot, j1, which, 3);
				} else {
					flag1 = j1 != -999
							&& (Keyboard.isKeyDown(42) || Keyboard
									.isKeyDown(54));

					if (flag1) {
						setField_94075_K(slot != null && slot.getHasStack() ? slot
								.getStack() : null);
						// this.field_94075_K = slot != null &&
						// slot.getHasStack() ? slot
						// .getStack() : null;
					}

					this.handleMouseClick(slot, j1, which, flag1 ? 1 : 0);
				}
			}
		}

		if (this.mc.thePlayer.inventory.getItemStack() == null) {
			setField_94070_G(0L); // this.field_94070_G = 0L;
		}

		this.field_94076_q = false;
	}

	/*
	 * Getter and Setters for Private Fields to perpetuate kludge.
	 */

	protected Slot getTheSlot() {
		return getPrivateValue(GuiContainer.class, this, "field_82320_o",
				"theSlot", "r");
	}

	protected void setTheSlot(Slot slot) {
		setPrivateValue(GuiContainer.class, this, slot, "field_82320_o",
				"theSlot", "r");
	}

	protected ItemStack getDraggedStack() {
		return getPrivateValue(GuiContainer.class, this, "field_85050_q",
				"draggedStack", "u");
	}

	protected void setDraggedStack(ItemStack stack) {
		setPrivateValue(GuiContainer.class, this, stack, "field_85050_q",
				"draggedStack", "u");
	}

	protected boolean getIsRightMouseClick() {
		return getPrivateValue(GuiContainer.class, this, "field_90018_r",
				"isRightMouseClick", "t");
	}

	protected void setIsRightMouseClick(boolean isRightMouseClick) {
		setPrivateValue(GuiContainer.class, this, isRightMouseClick,
				"field_90018_r", "isRightMouseClick", "t");
	}

	protected int getField_94069_F() {
		return getPrivateValue(GuiContainer.class, this, "field_94069_F", "F");
	}

	protected void setField_94069_F(int i) {
		setPrivateValue(GuiContainer.class, this, i, "field_94069_F", "F");
	}

	protected ItemStack getReturningStack() {
		return getPrivateValue(GuiContainer.class, this, "field_85045_v",
				"returningStack", "z");
	}

	protected void setReturningStack(ItemStack stack) {
		setPrivateValue(GuiContainer.class, this, stack, "field_85045_v",
				"returningStack", "z");
	}

	protected long getReturningStackTime() {
		return getPrivateValue(GuiContainer.class, this, "field_85046_u",
				"returningStackTime", "y");
	}

	protected void setReturningStackTime(long l) {
		setPrivateValue(GuiContainer.class, this, l, "field_85046_u",
				"returningStackTime", "y");
	}

	protected Slot getReturningStackDestSlot() {
		return getPrivateValue(GuiContainer.class, this, "field_85047_t",
				"returningStackDestSlot", "x");
	}

	protected void setReturningStackDestSlot(Slot slot) {
		setPrivateValue(GuiContainer.class, this, slot, "field_85047_t",
				"returningStackDestSlot", "x");
	}

	protected int getField_85049_r() {
		return getPrivateValue(GuiContainer.class, this, "field_85049_r", "v");
	}

	protected void setField_85049_r(int i) {
		setPrivateValue(GuiContainer.class, this, i, "field_85049_r", "v");
	}

	protected int getField_85048_s() {
		return getPrivateValue(GuiContainer.class, this, "field_85048_s", "w");
	}

	protected void setField_85048_s(int i) {
		setPrivateValue(GuiContainer.class, this, i, "field_85048_s", "w");
	}

	/*
	 * field_94074_J boolean
	 * 
	 * field_94072_H Slot
	 * 
	 * field_94070_G long
	 * 
	 * field_94068_E boolean
	 * 
	 * clickedSlot field_85051_p Slot
	 * 
	 * field_94075_K ItemStack
	 * 
	 * field_94067_D int
	 * 
	 * field_94071_C int
	 * 
	 * field_94073_I int
	 * 
	 * field_92033_y Slot
	 * 
	 * field_92032_z long
	 */

	protected boolean getField_94074_J() {
		return getPrivateValue(GuiContainer.class, this, "field_94074_J", "L");
	}

	protected void setField_94074_J(boolean bool) {
		setPrivateValue(GuiContainer.class, this, bool, "field_94074_J", "L");
	}

	protected Slot getField_94072_H() {
		return getPrivateValue(GuiContainer.class, this, "field_94072_H", "J");
	}

	protected void setField_94072_H(Slot slot) {
		setPrivateValue(GuiContainer.class, this, slot, "field_94072_H", "J");
	}

	protected long getField_94070_G() {
		return getPrivateValue(GuiContainer.class, this, "field_94070_G", "I");
	}

	protected void setField_94070_G(long l) {
		setPrivateValue(GuiContainer.class, this, l, "field_94070_G", "I");
	}

	protected boolean getField_94068_E() {
		return getPrivateValue(GuiContainer.class, this, "field_94068_E", "G");
	}

	protected void setField_94068_E(boolean bool) {
		setPrivateValue(GuiContainer.class, this, bool, "field_94068_E", "G");
	}

	protected Slot getClickedSlot() {
		return getPrivateValue(GuiContainer.class, this, "field_85051_p",
				"clickedSlot", "u");
	}

	protected void setClickedSlot(Slot slot) {
		setPrivateValue(GuiContainer.class, this, slot, "field_85051_p",
				"clickedSlot", "u");
	}

	protected ItemStack getField_94075_K() {
		return getPrivateValue(GuiContainer.class, this, "field_94075_K", "M");
	}

	protected void setField_94075_K(ItemStack itemStack) {
		setPrivateValue(GuiContainer.class, this, itemStack, "field_94075_K",
				"M");
	}

	protected int getField_94067_D() {
		return getPrivateValue(GuiContainer.class, this, "field_94067_D", "F");
	}

	protected void setField_94067_D(int i) {
		setPrivateValue(GuiContainer.class, this, i, "field_94067_D", "F");
	}

	protected int getField_94071_C() {
		return getPrivateValue(GuiContainer.class, this, "field_94071_C", "E");
	}

	protected void setField_94071_C(int i) {
		setPrivateValue(GuiContainer.class, this, i, "field_94071_C", "E");
	}

	protected int getField_94073_I() {
		return getPrivateValue(GuiContainer.class, this, "field_94073_I", "K");
	}

	protected void setField_94073_I(int i) {
		setPrivateValue(GuiContainer.class, this, i, "field_94073_I", "K");
	}

	protected Slot getField_92033_y() {
		return getPrivateValue(GuiContainer.class, this, "field_92033_y", "C");
	}

	protected void setField_92033_y(Slot slot) {
		setPrivateValue(GuiContainer.class, this, slot, "field_92033_y", "C");
	}

	protected long getField_92032_z() {
		return getPrivateValue(GuiContainer.class, this, "field_92032_z", "D");
	}

	protected void setField_92032_z(long l) {
		setPrivateValue(GuiContainer.class, this, l, "field_92032_z", "D");
	}

	protected GuiButton getSelectedButton() {
		return getPrivateValue(GuiScreen.class, this, "field_73883_a",
				"selectedButton", "a");
	}

	protected void setSelectedButton(GuiButton button) {
		setPrivateValue(GuiScreen.class, this, button, "field_73883_a",
				"selectedButton", "a");
	}
}
