//package chrisclark13.minecraft.customslots.client.gui.inventory;
//
//import java.util.Iterator;
//import java.util.Set;
//
//import org.lwjgl.input.Keyboard;
//import org.lwjgl.opengl.GL11;
//import org.lwjgl.opengl.GL12;
//
//import chrisclark13.minecraft.customslots.helper.MultiSlotItemHelper;
//import chrisclark13.minecraft.customslots.inventory.IDisableableSlot;
//import chrisclark13.minecraft.customslots.inventory.ISizedSlot;
//import chrisclark13.minecraft.customslots.inventory.SlotMultiSlotItem;
//import chrisclark13.minecraft.customslots.inventory.SlotSignature;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.gui.FontRenderer;
//import net.minecraft.client.gui.GuiButton;
//import net.minecraft.client.gui.GuiScreen;
//import net.minecraft.client.gui.inventory.GuiContainer;
//import net.minecraft.client.renderer.OpenGlHelper;
//import net.minecraft.client.renderer.RenderHelper;
//import net.minecraft.client.renderer.Tessellator;
//import net.minecraft.entity.player.InventoryPlayer;
//import net.minecraft.inventory.Container;
//import net.minecraft.inventory.Slot;
//import net.minecraft.item.ItemStack;
//import net.minecraft.util.EnumChatFormatting;
//import net.minecraft.util.MathHelper;
//import net.minecraft.util.ResourceLocation;
//import static cpw.mods.fml.common.ObfuscationReflectionHelper.*;
//
//public abstract class GuiMultiSlotItem extends GuiContainer {
//    
//	protected ResourceLocation glint = new ResourceLocation("textures/misc/enchanted_item_glint.png");
//    protected float glintTimer = 0;
//    
//    public GuiMultiSlotItem(Container par1Container) {
//        super(par1Container);
//    }
//    
//    /**
//     * Draws the screen and all <code>true</code>he components in it.
//     */
//    public void drawScreen(int mouseX, int mouseY, float par3) {
//        
//        // Private values in GuiContainerdr
//        Slot theSlot = null;
//        ItemStack draggedStack = getDraggedStack();
//        boolean isRightMouseClick = getIsRightMouseClick();
//        int field_94069_F = getField_94069_F();
//        
//        this.drawDefaultBackground();
//        int guiLeft = this.guiLeft;
//        int guiTop = this.guiTop;
//        this.drawGuiContainerBackgroundLayer(par3, mouseX, mouseY);
//        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
//        RenderHelper.disableStandardItemLighting();
//        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
//        
//        for (int k = 0; k < this.buttonList.size(); ++k) {
//            GuiButton guibutton = (GuiButton) this.buttonList.get(k);
//            guibutton.drawButton(this.mc, mouseX, mouseY);
//        }
//        RenderHelper.enableGUIStandardItemLighting();
//        GL11.glPushMatrix();
//        GL11.glTranslatef((float) guiLeft, (float) guiTop, 0.0F);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
//        this.setTheSlot(null);
//        short short1 = 240;
//        short short2 = 240;
//        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) short1 / 1.0F,
//                (float) short2 / 1.0F);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        int yPos;
//        InventoryPlayer inventoryplayer = this.mc.thePlayer.inventory;
//        ItemStack heldItemStack = draggedStack == null ? inventoryplayer.getItemStack() : draggedStack;
//        
//        glintTimer = Minecraft.getSystemTime();
//        for (int j1 = 0; j1 < this.inventorySlots.inventorySlots.size(); ++j1) {
//            Slot slot = (Slot) this.inventorySlots.inventorySlots.get(j1);
//            
//            if (this.isMouseOverSlot(slot, mouseX, mouseY)) {
//                this.setTheSlot(slot);
//                theSlot = slot;
//            }
//            
//            this.drawSlotInventory(slot);
//        }
//        
//        if (theSlot != null) {
//            drawSelectionHighlight(theSlot, heldItemStack);
//        }
//        
//        GL11.glDisable(GL11.GL_LIGHTING);
//        this.drawGuiContainerForegroundLayer(mouseX, mouseY);
//        GL11.glEnable(GL11.GL_LIGHTING);
//        
//        if (heldItemStack != null) {
//            byte xOffset = 8;
//            yPos = draggedStack == null ? 8 : 16;
//            String s = null;
//            
//            if (draggedStack != null && isRightMouseClick) {
//                heldItemStack = heldItemStack.copy();
//                heldItemStack.stackSize = MathHelper
//                        .ceiling_float_int((float) heldItemStack.stackSize / 2.0F);
//            } else if (this.field_94076_q && this.field_94077_p.size() > 1) {
//                heldItemStack = heldItemStack.copy();
//                heldItemStack.stackSize = field_94069_F;
//                
//                if (heldItemStack.stackSize == 0) {
//                    s = "" + EnumChatFormatting.YELLOW + "0";
//                }
//            }
//            
//            if (theSlot != null && theSlot instanceof SlotMultiSlotItem) {
//                drawItemStackInHandOverMultiSlot(heldItemStack, mouseX - guiLeft - xOffset, mouseY
//                        - guiTop - yPos, s);
//            } else {
//                this.drawItemStackInHand(heldItemStack, mouseX - guiLeft - xOffset, mouseY - guiTop
//                        - yPos, s);
//            }
//        }
//        
//        ItemStack returningStack = getReturningStack();
//        long returningStackTime = getReturningStackTime();
//        Slot returningStackDestSlot = getReturningStackDestSlot();
//        
//        int field_85049_r = getField_85049_r();
//        int field_85048_s = getField_85048_s();
//        
//        if (returningStack != null) {
//            float f1 = (float) (Minecraft.getSystemTime() - returningStackTime) / 100.0F;
//            
//            if (f1 >= 1.0F) {
//                f1 = 1.0F;
//                // this.returningStack = null;
//                setReturningStack(null);
//                returningStack = null;
//            }
//            
//            yPos = returningStackDestSlot.xDisplayPosition - field_85049_r;
//            int l1 = returningStackDestSlot.yDisplayPosition - field_85048_s;
//            int i2 = field_85049_r + (int) ((float) yPos * f1);
//            int j2 = field_85048_s + (int) ((float) l1 * f1);
//            this.drawItemStackInHand(returningStack, i2, j2, (String) null);
//        }
//        
//        GL11.glPopMatrix();
//        
//        if (inventoryplayer.getItemStack() == null && theSlot != null) {
//            if (theSlot.getHasStack()) {
//                ItemStack itemstack1 = theSlot.getStack();
//                this.drawItemStackTooltip(itemstack1, mouseX, mouseY);
//            } else if (theSlot instanceof SlotMultiSlotItem
//                    && ((SlotMultiSlotItem) theSlot).getParentSlotIfExists().getHasStack()) {
//                ItemStack itemstack1 = ((SlotMultiSlotItem) theSlot).getParentSlotIfExists().getStack();
//                this.drawItemStackTooltip(itemstack1, mouseX, mouseY);
//            }
//        }
//        
//        GL11.glEnable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        RenderHelper.enableStandardItemLighting();
//    }
//
//    private void drawSelectionHighlight(Slot theSlot, ItemStack heldItemStack) {
//        
//        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glDisable(GL11.GL_DEPTH_TEST);
//        int xPos = theSlot.xDisplayPosition;
//        int yPos = theSlot.yDisplayPosition;
//        
//        // Selection Gradient Drawing
//        // SlotMultiSlotItem Drawing
//        if (theSlot instanceof SlotMultiSlotItem) {
//            SlotMultiSlotItem slotMSI = (SlotMultiSlotItem) theSlot;
//            
//            if (slotMSI.getParentSlotIfExists().getHasStack()) {
//                Set<SlotMultiSlotItem> slots = ((SlotMultiSlotItem) theSlot)
//                        .getLinkedSlotsIncludingThis();
//                for (SlotMultiSlotItem linkedSlot : slots) {
//                    if (shouldDrawSlotHighlight(linkedSlot)) {
//                        this.drawGradientRect(linkedSlot.xDisplayPosition,
//                                linkedSlot.yDisplayPosition, linkedSlot.xDisplayPosition
//                                        + linkedSlot.getWidth(), linkedSlot.yDisplayPosition
//                                        + linkedSlot.getHeight(), -2130706433, -2130706433);
//                    }
//                }
//            } else if (heldItemStack != null) {
//                
//                if (slotMSI.getGridSlot().isItemStackValid(heldItemStack) && shouldDrawSlotHighlight(slotMSI)) {
//                    SlotSignature sig = MultiSlotItemHelper
//                            .getSignature(heldItemStack);
//                    
//                    int minX = xPos - slotMSI.getGridSlot().getGridX() * slotMSI.getWidth();
//                    int minY = yPos - slotMSI.getGridSlot().getGridY() * slotMSI.getHeight();
//                    int maxX = xPos
//                            + (slotMSI.getGridSlot().getGrid().getWidth() - slotMSI
//                                    .getGridSlot().getGridX()) * slotMSI.getWidth();
//                    int maxY = yPos
//                            + (slotMSI.getGridSlot().getGrid().getHeight() - slotMSI
//                                    .getGridSlot().getGridY()) * slotMSI.getHeight();
//                    
//                    for (int sy = 0; sy < sig.getHeight(); sy++) {
//                        for (int sx = 0; sx < sig.getWidth(); sx++) {
//                            if (sig.isSlotDesignated(sx, sy)) {
//                                int _x = xPos - ((sig.getCenterX() - sx) * slotMSI.getWidth());
//                                int _y = yPos - ((sig.getCenterY() - sy) * slotMSI.getHeight());
//                                
//                                if (_x >= minX && _x < maxX && _y >= minY && _y < maxY) {
//                                    this.drawGradientRect(_x, _y, _x + slotMSI.getWidth(), _y
//                                            + slotMSI.getHeight(), -2130706433, -2130706433);
//                                }
//                                
//                            }
//                        }
//                    }
//                }
//            } else {
//                if (shouldDrawSlotHighlight(slotMSI)) {
//                    this.drawGradientRect(xPos, yPos, xPos + slotMSI.getWidth(),
//                            yPos + slotMSI.getHeight(), -2130706433, -2130706433);
//                }
//            }
//            
//        } else if (theSlot instanceof ISizedSlot && shouldDrawSlotHighlight(theSlot)) {
//            // ISizeableSlot drawing
//            ISizedSlot slotSizeable = (ISizedSlot) theSlot;
//            this.drawGradientRect(xPos, yPos, xPos + slotSizeable.getWidth(), yPos
//                    + slotSizeable.getHeight(), -2130706433, -2130706433);
//        } else if (shouldDrawSlotHighlight(theSlot)){
//            // Normal Slot drawing
//            this.drawGradientRect(xPos, yPos, xPos + 16, yPos + 16, -2130706433, -2130706433);
//        }
//        
//        GL11.glEnable(GL11.GL_LIGHTING);
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//    }
//    
//    /**
//     * Returns the slot at the given coordinates or null if there is none.
//     */
//    protected Slot getSlotAtPosition(int par1, int par2)
//    {
//        for (int k = 0; k < this.inventorySlots.inventorySlots.size(); ++k)
//        {
//            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(k);
//
//            if (this.isMouseOverSlot(slot, par1, par2))
//            {
//                return slot;
//            }
//        }
//
//        return null;
//    }
//    
//    /**
//     * Called when the mouse is clicked.
//     */
//    protected void mouseClicked(int par1, int par2, int par3)
//    {
//        if (par3 == 0)
//        {
//            for (int l1 = 0; l1 < this.buttonList.size(); ++l1)
//            {
//                GuiButton guibutton = (GuiButton)this.buttonList.get(l1);
//        
//                if (guibutton.mousePressed(this.mc, par1, par2))
//                {
//                    this.selectedButton = guibutton;
//                    this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
//                    this.actionPerformed(guibutton);
//                }
//            }
//        }
//        
//        boolean flag = par3 == this.mc.gameSettings.keyBindPickBlock.keyCode + 100;
//        Slot slot = this.getSlotAtPosition(par1, par2);
//        long l = Minecraft.getSystemTime();
//        this.field_94074_J = this.field_94072_H == slot && l - this.field_94070_G < 250L && this.field_94073_I == par3;
//        this.field_94068_E = false;
//
//        if (par3 == 0 || par3 == 1 || flag)
//        {
//            int i1 = this.guiLeft;
//            int j1 = this.guiTop;
//            boolean flag1 = par1 < i1 || par2 < j1 || par1 >= i1 + this.xSize || par2 >= j1 + this.ySize;
//            int k1 = -1;
//
//            if (slot != null)
//            {
//                k1 = slot.slotNumber;
//            }
//
//            if (flag1)
//            {
//                k1 = -999;
//            }
//
//            if (this.mc.gameSettings.touchscreen && flag1 && this.mc.thePlayer.inventory.getItemStack() == null)
//            {
//                this.mc.displayGuiScreen((GuiScreen)null);
//                return;
//            }
//
//            if (k1 != -1)
//            {
//                if (this.mc.gameSettings.touchscreen)
//                {
//                    if (slot != null && slot.getHasStack())
//                    {
//                        this.clickedSlot = slot;
//                        this.draggedStack = null;
//                        this.isRightMouseClick = par3 == 1;
//                    }
//                    else
//                    {
//                        this.clickedSlot = null;
//                    }
//                }
//                else if (!this.field_94076_q)
//                {
//                    if (this.mc.thePlayer.inventory.getItemStack() == null)
//                    {
//                        if (par3 == this.mc.gameSettings.keyBindPickBlock.keyCode + 100)
//                        {
//                            this.handleMouseClick(slot, k1, par3, 3);
//                        }
//                        else
//                        {
//                            boolean flag2 = k1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
//                            byte b0 = 0;
//
//                            if (flag2)
//                            {
//                                this.field_94075_K = slot != null && slot.getHasStack() ? slot.getStack() : null;
//                                b0 = 1;
//                            }
//                            else if (k1 == -999)
//                            {
//                                b0 = 4;
//                            }
//
//                            this.handleMouseClick(slot, k1, par3, b0);
//                        }
//
//                        this.field_94068_E = true;
//                    }
//                    else
//                    {
//                        this.field_94076_q = true;
//                        this.field_94067_D = par3;
//                        this.field_94077_p.clear();
//
//                        if (par3 == 0)
//                        {
//                            this.field_94071_C = 0;
//                        }
//                        else if (par3 == 1)
//                        {
//                            this.field_94071_C = 1;
//                        }
//                    }
//                }
//            }
//        }
//
//        this.field_94072_H = slot;
//        this.field_94070_G = l;
//        this.field_94073_I = par3;
//    }
//    
//    @SuppressWarnings("unchecked")
//    @Override
//    protected void mouseClickMove(int par1, int par2, int par3, long par4)
//    {
//        Slot slot = this.getSlotAtPosition(par1, par2);
//        ItemStack itemstack = this.mc.thePlayer.inventory.getItemStack();
//
//        if (this.clickedSlot != null && this.mc.gameSettings.touchscreen)
//        {
//            if (par3 == 0 || par3 == 1)
//            {
//                if (this.draggedStack == null)
//                {
//                    if (slot != this.clickedSlot)
//                    {
//                        this.draggedStack = this.clickedSlot.getStack().copy();
//                    }
//                }
//                else if (this.draggedStack.stackSize > 1 && slot != null && Container.func_94527_a(slot, this.draggedStack, false))
//                {
//                    long i1 = Minecraft.getSystemTime();
//
//                    if (this.field_92033_y == slot)
//                    {
//                        if (i1 - this.field_92032_z > 500L)
//                        {
//                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
//                            this.handleMouseClick(slot, slot.slotNumber, 1, 0);
//                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, 0, 0);
//                            this.field_92032_z = i1 + 750L;
//                            --this.draggedStack.stackSize;
//                        }
//                    }
//                    else
//                    {
//                        this.field_92033_y = slot;
//                        this.field_92032_z = i1;
//                    }
//                }
//            }
//        }
//        else if (this.field_94076_q && slot != null && itemstack != null && itemstack.stackSize > this.field_94077_p.size() && Container.func_94527_a(slot, itemstack, true) && slot.isItemValid(itemstack) && this.inventorySlots.canDragIntoSlot(slot))
//        {
//            this.field_94077_p.add(slot);
//            this.func_94066_g();
//        }
//    }
//
//    /**
//     * Called when the mouse is moved or a mouse button is released.  Signature: (mouseX, mouseY, which) which==-1 is
//     * mouseMove, which==0 or which==1 is mouseUp
//     */
//    @SuppressWarnings("rawtypes")
//    protected void mouseMovedOrUp(int par1, int par2, int par3)
//    {
//        if (this.selectedButton != null && par3 == 0)
//        {
//            this.selectedButton.mouseReleased(par1, par2);
//            this.selectedButton = null;
//        }
//        
//        Slot slot = this.getSlotAtPosition(par1, par2);
//        int l = this.guiLeft;
//        int i1 = this.guiTop;
//        boolean flag = par1 < l || par2 < i1 || par1 >= l + this.xSize || par2 >= i1 + this.ySize;
//        int j1 = -1;
//
//        if (slot != null)
//        {
//            j1 = slot.slotNumber;
//        }
//
//        if (flag)
//        {
//            j1 = -999;
//        }
//
//        Slot slot1;
//        Iterator iterator;
//
//        if (this.field_94074_J && slot != null && par3 == 0 && this.inventorySlots.func_94530_a((ItemStack)null, slot))
//        {
//            if (isShiftKeyDown())
//            {
//                if (slot != null && slot.inventory != null && this.field_94075_K != null)
//                {
//                    iterator = this.inventorySlots.inventorySlots.iterator();
//
//                    while (iterator.hasNext())
//                    {
//                        slot1 = (Slot)iterator.next();
//
//                        if (slot1 != null && slot1.canTakeStack(this.mc.thePlayer) && slot1.getHasStack() && slot1.inventory == slot.inventory && Container.func_94527_a(slot1, this.field_94075_K, true))
//                        {
//                            this.handleMouseClick(slot1, slot1.slotNumber, par3, 1);
//                        }
//                    }
//                }
//            }
//            else
//            {
//                this.handleMouseClick(slot, j1, par3, 6);
//            }
//
//            this.field_94074_J = false;
//            this.field_94070_G = 0L;
//        }
//        else
//        {
//            if (this.field_94076_q && this.field_94067_D != par3)
//            {
//                this.field_94076_q = false;
//                this.field_94077_p.clear();
//                this.field_94068_E = true;
//                return;
//            }
//
//            if (this.field_94068_E)
//            {
//                this.field_94068_E = false;
//                return;
//            }
//
//            boolean flag1;
//
//            if (this.clickedSlot != null && this.mc.gameSettings.touchscreen)
//            {
//                if (par3 == 0 || par3 == 1)
//                {
//                    if (this.draggedStack == null && slot != this.clickedSlot)
//                    {
//                        this.draggedStack = this.clickedSlot.getStack();
//                    }
//
//                    flag1 = Container.func_94527_a(slot, this.draggedStack, false);
//
//                    if (j1 != -1 && this.draggedStack != null && flag1)
//                    {
//                        this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, par3, 0);
//                        this.handleMouseClick(slot, j1, 0, 0);
//
//                        if (this.mc.thePlayer.inventory.getItemStack() != null)
//                        {
//                            this.handleMouseClick(this.clickedSlot, this.clickedSlot.slotNumber, par3, 0);
//                            this.field_85049_r = par1 - l;
//                            this.field_85048_s = par2 - i1;
//                            this.returningStackDestSlot = this.clickedSlot;
//                            this.returningStack = this.draggedStack;
//                            this.returningStackTime = Minecraft.getSystemTime();
//                        }
//                        else
//                        {
//                            this.returningStack = null;
//                        }
//                    }
//                    else if (this.draggedStack != null)
//                    {
//                        this.field_85049_r = par1 - l;
//                        this.field_85048_s = par2 - i1;
//                        this.returningStackDestSlot = this.clickedSlot;
//                        this.returningStack = this.draggedStack;
//                        this.returningStackTime = Minecraft.getSystemTime();
//                    }
//
//                    this.draggedStack = null;
//                    this.clickedSlot = null;
//                }
//            }
//            else if (this.field_94076_q && !this.field_94077_p.isEmpty())
//            {
//                this.handleMouseClick((Slot)null, -999, Container.func_94534_d(0, this.field_94071_C), 5);
//                iterator = this.field_94077_p.iterator();
//
//                while (iterator.hasNext())
//                {
//                    slot1 = (Slot)iterator.next();
//                    this.handleMouseClick(slot1, slot1.slotNumber, Container.func_94534_d(1, this.field_94071_C), 5);
//                }
//
//                this.handleMouseClick((Slot)null, -999, Container.func_94534_d(2, this.field_94071_C), 5);
//            }
//            else if (this.mc.thePlayer.inventory.getItemStack() != null)
//            {
//                if (par3 == this.mc.gameSettings.keyBindPickBlock.keyCode + 100)
//                {
//                    this.handleMouseClick(slot, j1, par3, 3);
//                }
//                else
//                {
//                    flag1 = j1 != -999 && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54));
//
//                    if (flag1)
//                    {
//                        this.field_94075_K = slot != null && slot.getHasStack() ? slot.getStack() : null;
//                    }
//
//                    this.handleMouseClick(slot, j1, par3, flag1 ? 1 : 0);
//                }
//            }
//        }
//
//        if (this.mc.thePlayer.inventory.getItemStack() == null)
//        {
//            this.field_94070_G = 0L;
//        }
//
//        this.field_94076_q = false;
//    }
//    
//    protected boolean isMouseOverSlot(Slot slot, int mouseX, int mouseY) {
//        if (slot instanceof IDisableableSlot && ((IDisableableSlot) slot).isDisabled()) {
//            return false;
//        }
//        
//        if (slot instanceof ISizedSlot) {
//            ISizedSlot slotSizable = (ISizedSlot) slot;
//            return isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition,
//                    slotSizable.getWidth(), slotSizable.getHeight(), mouseX, mouseY,
//                    slotSizable.getSelectionBorder());
//        } else {
//            return isPointInRegion(slot.xDisplayPosition, slot.yDisplayPosition, 16, 16, mouseX,
//                    mouseY, 1);
//        }
//    }
//    
//    @Override
//    protected boolean isPointInRegion(int par1, int par2, int par3, int par4, int par5, int par6) {
//        return isPointInRegion(par1, par2, par3, par4, par5, par6, 1);
//    }
//    
//    /**
//     * Args: left, top, width, height, pointX, pointY. Note: left, top are local
//     * to Gui, pointX, pointY are local to screen
//     */
//    protected boolean isPointInRegion(int left, int top, int width, int height, int pointX,
//            int pointY, int offset) {
//        pointX -= this.guiLeft;
//        pointY -= this.guiTop;
//        return pointX >= left - offset && pointX < left + width + offset && pointY >= top - offset
//                && pointY < top + height + offset;
//    }
//    
//    protected void handleMouseClick(Slot slot, int par2, int par3, int par4) {
//        if (slot instanceof IDisableableSlot
//                && (((IDisableableSlot) slot).isDisabled() || !((IDisableableSlot) slot)
//                        .isClickable())) {
//            return;
//        }
//        
//        if (slot != null) {
//            par2 = slot.slotNumber;
//        }
//        
//        this.mc.playerController.windowClick(this.inventorySlots.windowId, par2, par3, par4,
//                this.mc.thePlayer);
//    }
//    
//    private void drawItemStackInHandOverMultiSlot(ItemStack itemStack, int x, int y, String str) {
//        Slot theSlot = getTheSlot();
//        
//        if (theSlot == null || !(theSlot instanceof SlotMultiSlotItem)) {
//            this.drawItemStackInHand(itemStack, x, y, str);
//            return;
//        }
//        
//        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
//        
//        SlotMultiSlotItem slotMSI = (SlotMultiSlotItem) theSlot;
//        ItemStack draggedStack = getDraggedStack();
//        
//        x += (16 - slotMSI.getWidth()) / 2;
//        y += (16 - slotMSI.getHeight()) / 2;
//        
//        this.zLevel = 200.0F;
//        itemRenderer.zLevel = 200.0F;
//        
//        FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
//        if (font == null)
//            font = fontRenderer;
//        
//        if (MultiSlotItemHelper.getImageResource(itemStack) != null) {
//        	mc.getTextureManager().bindTexture(MultiSlotItemHelper.getImageResource(itemStack));
//            
//            SlotSignature sig = MultiSlotItemHelper.getSignature(itemStack);
//            int xPos = x + sig.getRelativeLeft() * slotMSI.getWidth();
//            int yPos = y + sig.getRelativeTop() * slotMSI.getHeight();
//            
//            GL11.glDisable(GL11.GL_LIGHTING);
//            
//            float[] colors = unpackColorsFromInt(MultiSlotItemHelper.getImageColor(itemStack));
//            GL11.glColor4f(colors[1], colors[2], colors[3], 1.0F);
//            this.drawImage(xPos, yPos, sig.getWidth() * slotMSI.getWidth(), sig.getHeight()
//                    * slotMSI.getHeight() - (draggedStack == null ? 0 : 8));
//            
//            GL11.glEnable(GL11.GL_LIGHTING);
//            
//            if (itemStack.hasEffect(0)) {
//                renderGlintOnImage(xPos, yPos, sig.getWidth() * slotMSI.getWidth(), sig.getHeight()
//                        * slotMSI.getHeight(), 0, 0 - (draggedStack == null ? 0 : 8));
//            }
//            
//        } else {
//            GL11.glPushMatrix();
//            
//            GL11.glTranslatef(x, y, 0F);
//            GL11.glScalef(slotMSI.getWidth() / 16F, slotMSI.getHeight() / 16F, 1F);
//            
//            itemRenderer.renderItemAndEffectIntoGUI(font, this.mc.renderEngine, itemStack, 0,
//                    0 - (draggedStack == null ? 0 : 8));
//            
//            GL11.glPopMatrix();
//        }
//        
//        GL11.glPushMatrix();
//        
//        GL11.glTranslatef(x, y, 0F);
//        GL11.glScalef(slotMSI.getWidth() / 16F, slotMSI.getHeight() / 16F, 1F);
//        
//        itemRenderer.renderItemOverlayIntoGUI(font, this.mc.renderEngine, itemStack, 0,
//                0 - (draggedStack == null ? 0 : 8), str);
//        
//        GL11.glPopMatrix();
//        
//        itemRenderer.zLevel = 0.0F;
//        this.zLevel = 0.0F;
//    }
//    
//    private void drawItemStackInHand(ItemStack itemStack, int x, int y, String str) {
//        Slot theSlot = getTheSlot();
//        ItemStack draggedStack = getDraggedStack();
//        
//        GL11.glTranslatef(0.0F, 0.0F, 32.0F);
//        this.zLevel = 200.0F;
//        itemRenderer.zLevel = 200.0F;
//        
//        FontRenderer font = itemStack.getItem().getFontRenderer(itemStack);
//        if (font == null)
//            font = fontRenderer;
//        
//        if (theSlot != null && theSlot instanceof ISizedSlot) {
//            ISizedSlot slotSizeable = (ISizedSlot) theSlot;
//            
//            GL11.glPushMatrix();
//            
//            GL11.glTranslatef(x, y, 0F);
//            GL11.glScalef(slotSizeable.getWidth() / 16F, slotSizeable.getHeight() / 16F, 1F);
//            
//            itemRenderer.renderItemAndEffectIntoGUI(font, this.mc.renderEngine, itemStack, 0,
//                    0 - (draggedStack == null ? 0 : 8));
//            itemRenderer.renderItemOverlayIntoGUI(font, this.mc.renderEngine, itemStack, 0,
//                    0 - (draggedStack == null ? 0 : 8), str);
//            
//            GL11.glPopMatrix();
//            
//        } else {
//            itemRenderer.renderItemAndEffectIntoGUI(font, this.mc.renderEngine, itemStack, x, y);
//            itemRenderer.renderItemOverlayIntoGUI(font, this.mc.renderEngine, itemStack, x, y
//                    - (draggedStack == null ? 0 : 8), str);
//        }
//        
//        this.zLevel = 0.0F;
//        itemRenderer.zLevel = 0.0F;
//    }
//    
//    @Override
//    protected void drawSlotInventory(Slot slot) {
//        
//        if (!(slot instanceof SlotMultiSlotItem)
//                || ((SlotMultiSlotItem) slot).getParentSlotIfExists().getStack() == null) {
//            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//            super.drawSlotInventory(slot);
//            return;
//        }
//        
//        if ((slot instanceof IDisableableSlot)
//                && (((IDisableableSlot) slot).isDisabled() || !((IDisableableSlot) slot)
//                        .getDrawItemStack())) {
//            return;
//        }
//        
//        SlotMultiSlotItem slotMSI = (SlotMultiSlotItem) slot;
//        SlotMultiSlotItem pSlotMSI = slotMSI.getParentSlotIfExists();
//        ItemStack itemStack = slotMSI.getParentSlotIfExists().getStack();
//        ResourceLocation imageResource = MultiSlotItemHelper.getImageResource(itemStack);
//        int x = slot.xDisplayPosition;
//        int y = slot.yDisplayPosition;
//        int px = pSlotMSI.xDisplayPosition;
//        int py = pSlotMSI.yDisplayPosition;
//        
//        this.zLevel = 100.0F;
//        itemRenderer.zLevel = 100.0F;
//        
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        
//        if (imageResource != null) {
//            
//            // GL11.glDisable(GL11.GL_DEPTH_TEST);
//            GL11.glDisable(GL11.GL_LIGHTING);
//            SlotSignature sig = MultiSlotItemHelper.getSignature(itemStack);
//            // this.drawTexturedModalRect(x + sig.getRelativeLeft() * 16, y +
//            // sig.getRelativeTop() * 16, 0, 0, sig.getWidth() * 16,
//            // sig.getHeight());
//            this.mc.getTextureManager().bindTexture(imageResource);
//            float[] colors = unpackColorsFromInt(MultiSlotItemHelper.getImageColor(itemStack));
//            GL11.glColor4f(colors[1], colors[2], colors[3], 1.0F);
//            int u = sig.getCenterX() * slotMSI.getWidth() + (x - px);
//            int v = sig.getCenterY() * slotMSI.getHeight() + (y - py);
//            this.drawImagePart(x, y, slotMSI.getWidth(), slotMSI.getHeight(), u, v, sig.getWidth()
//                    * slotMSI.getWidth(), sig.getHeight() * slotMSI.getHeight());
//            GL11.glEnable(GL11.GL_LIGHTING);
//            // GL11.glEnable(GL11.GL_DEPTH_TEST);
//            
//            if (itemStack.hasEffect(0)) {
//                this.renderGlintOnImage(x, y, slotMSI.getWidth(), slotMSI.getHeight(), u, v);
//            }
//            
//        } else {
//            // If the imagePath is null then just draw the item normally
//            GL11.glPushMatrix();
//            
//            GL11.glTranslatef(x, y, 1F);
//            GL11.glScalef(slotMSI.getWidth() / 16F, slotMSI.getHeight() / 16F, 1F);
//            
//            itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, itemStack, 0, 0);
//            
//            GL11.glPopMatrix();
//        }
//        
//        // Render the overlay on the parent slot
//        if (slotMSI.getGridSlot().getParentSlot() == null) {
//            GL11.glPushMatrix();
//            
//            GL11.glTranslatef(x, y, 1F);
//            GL11.glScalef(slotMSI.getWidth() / 16F, slotMSI.getHeight() / 16F, 1F);
//            
//            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, itemStack, 0, 0);
//            
//            GL11.glPopMatrix();
//        }
//        
//        GL11.glEnable(GL11.GL_DEPTH_TEST);
//        GL11.glEnable(GL11.GL_LIGHTING);
//        
//        itemRenderer.zLevel = 0.0F;
//        this.zLevel = 0.0F;
//        
//    }
//    
//    /**
//     * Copied from RenderItem because it was private before.
//     * 
//     * @param x
//     * @param y
//     * @param width
//     * @param height
//     */
//    
//    public void renderGlintOnImage(int x, int y, int width, int height, int u, int v) {
//        GL11.glDepthFunc(GL11.GL_GREATER);
//        GL11.glDisable(GL11.GL_LIGHTING);
//        GL11.glDepthMask(false);
//        mc.getTextureManager().bindTexture(glint);
//        this.zLevel -= 50.0F;
//        GL11.glEnable(GL11.GL_BLEND);
//        GL11.glBlendFunc(GL11.GL_DST_COLOR, GL11.GL_DST_COLOR);
//        // May want to change this around!
//        GL11.glColor4f(0.5F, 0.25F, 0.8F, 1.0F);
//        
//        for (int j1 = 0; j1 < 2; ++j1) {
//            if (j1 == 0) {
//                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
//            }
//            
//            if (j1 == 1) {
//                GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
//            }
//            
//            float uScale = 0.00390625F;
//            float vScale = 0.00390625F;
//            float uOffset = (float) ((Minecraft.getSystemTime() % (long) (3000 + j1 * 1873))
//                    / (3000.0F + (float) (j1 * 1873)) * 256.0F)
//                    + (float) u;
//            Tessellator tessellator = Tessellator.instance;
//            float f4 = 4.0F;
//            
//            if (j1 == 1) {
//                f4 = -1.0F;
//            }
//            
//            tessellator.startDrawingQuads();
//            tessellator.addVertexWithUV((double) (x + 0), (double) (y + height),
//                    (double) this.zLevel,
//                    (double) ((uOffset + (float) (height) * f4 + (v * f4)) * uScale),
//                    (double) ((v + (float) height) * vScale));
//            tessellator.addVertexWithUV((double) (x + width), (double) (y + height),
//                    (double) this.zLevel, (double) ((uOffset + (float) width + (float) (height)
//                            * f4 + (v * f4)) * uScale), (double) ((v + (float) height) * vScale));
//            tessellator.addVertexWithUV((double) (x + width), (double) (y + 0),
//                    (double) this.zLevel, (double) ((uOffset + (float) width + (v * f4)) * uScale),
//                    (double) ((v + 0.0F) * vScale));
//            tessellator
//                    .addVertexWithUV((double) (x + 0), (double) (y + 0), (double) this.zLevel,
//                            (double) ((uOffset + 0.0F + (v * f4)) * uScale),
//                            (double) ((v + 0.0F) * vScale));
//            tessellator.draw();
//        }
//        
//        GL11.glDisable(GL11.GL_BLEND);
//        GL11.glDepthMask(true);
//        this.zLevel += 50.0F;
//        GL11.glEnable(GL11.GL_LIGHTING);
//        GL11.glDepthFunc(GL11.GL_LEQUAL);
//    }
//    
//    public void drawImage(int x, int y, int width, int height) {
//        drawImagePart(x, y, width, height, 0, 0, width, height);
//    }
//    
//    public void drawImagePart(int x, int y, int width, int height, int u, int v, int imageWidth,
//            int imageHeight) {
//        float uScale = 1F / (float) imageWidth;
//        float vScale = 1F / (float) imageHeight;
//        Tessellator tessellator = Tessellator.instance;
//        tessellator.startDrawingQuads();
//        tessellator.addVertexWithUV((double) (x + 0), (double) (y + height), (double) this.zLevel,
//                (double) ((float) (u + 0) * uScale), (double) ((float) (v + height) * vScale));
//        tessellator.addVertexWithUV((double) (x + width), (double) (y + height),
//                (double) this.zLevel, (double) ((float) (u + width) * uScale),
//                (double) ((float) (v + height) * vScale));
//        tessellator.addVertexWithUV((double) (x + width), (double) (y + 0), (double) this.zLevel,
//                (double) ((float) (u + width) * uScale), (double) ((float) (v + 0) * vScale));
//        tessellator.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) this.zLevel,
//                (double) ((float) (u + 0) * uScale), (double) ((float) (v + 0) * vScale));
//        tessellator.draw();
//    }
//    
//    /**
//     * Unpacks the colors packed into an integer (0-255) as floats (0.0F-1.0F)
//     * 
//     * @param color
//     *            An integer with the colors packed into it like so: 0xAARRGGBB
//     * @return An array with the unpacked colors. (0 = Alpha, 1 = Red, 2 =
//     *         Green, 3 = Blue)
//     */
//    public float[] unpackColorsFromInt(int color) {
//        float[] colors = new float[4];
//        
//        colors[0] = ((color >>> 24) & 0xFF) / 255F;
//        colors[1] = ((color >>> 16) & 0xFF) / 255F;
//        colors[2] = ((color >>> 8) & 0xFF) / 255F;
//        colors[3] = ((color) & 0xFF) / 255F;
//        
//        return colors;
//    }
//    
//    public int packColorFrom4Floats(float[] colors) {
//        int a = Math.min(256, Math.round(colors[0] * 256)) << 24;
//        int r = Math.min(256, Math.round(colors[1] * 256)) << 16;
//        int g = Math.min(256, Math.round(colors[2] * 256)) << 8;
//        int b = Math.min(256, Math.round(colors[3] * 256));
//        
//        return a | r | g | b;
//    }
//    
//    public int packColorFrom3Floats(float[] colors) {
//        int a = 0xFF000000;
//        int r = Math.min(256, Math.round(colors[0] * 256)) << 16;
//        int g = Math.min(256, Math.round(colors[1] * 256)) << 8;
//        int b = Math.min(256, Math.round(colors[2] * 256));
//        
//        return a | r | g | b;
//    }
//    
//    public boolean shouldDrawSlotHighlight(Slot slot) {
//        return !(slot instanceof IDisableableSlot)
//                || (!((IDisableableSlot) slot).isDisabled() && ((IDisableableSlot) slot)
//                        .getDrawHighlight());
//    }
//    
//    public Slot getTheSlot() {
//        return getPrivateValue(GuiContainer.class, this, "field_82320_o", "theSlot", "r");
//    }
//    
//    public void setTheSlot(Slot slot) {
//        setPrivateValue(GuiContainer.class, this, slot, "field_82320_o", "theSlot", "r");
//    }
//    
//    protected ItemStack getDraggedStack() {
//        return getPrivateValue(GuiContainer.class, this, "field_85050_q", "draggedStack", "u");
//    }
//    
//    protected void setDraggedStack(ItemStack stack) {
//        setPrivateValue(GuiContainer.class, this, stack, "field_85050_q", "draggedStack", "u");
//    }
//    
//    protected boolean getIsRightMouseClick() {
//        return getPrivateValue(GuiContainer.class, this, "field_90018_r", "isRightMouseClick", "t");
//    }
//    
//    protected void setIsRightMouseClick(boolean isRightMouseClick) {
//        setPrivateValue(GuiContainer.class, this, isRightMouseClick, "field_90018_r",
//                "isRightMouseClick", "t");
//    }
//    
//    protected int getField_94069_F() {
//        return getPrivateValue(GuiContainer.class, this, "field_94069_F", "F");
//    }
//    
//    protected void setField_94069_F(int i) {
//        setPrivateValue(GuiContainer.class, this, i, "field_94069_F", "F");
//    }
//    
//    protected ItemStack getReturningStack() {
//        return getPrivateValue(GuiContainer.class, this, "field_85045_v", "returningStack", "z");
//    }
//    
//    protected void setReturningStack(ItemStack stack) {
//        setPrivateValue(GuiContainer.class, this, stack, "field_85045_v", "returningStack", "z");
//    }
//    
//    protected long getReturningStackTime() {
//        return getPrivateValue(GuiContainer.class, this, "field_85046_u", "returningStackTime", "y");
//    }
//    
//    protected void setReturningStackTime(long l) {
//        setPrivateValue(GuiContainer.class, this, l, "field_85046_u", "returningStackTime", "y");
//    }
//    
//    protected Slot getReturningStackDestSlot() {
//        return getPrivateValue(GuiContainer.class, this, "field_85047_t", "returningStackDestSlot",
//                "x");
//    }
//    
//    protected void setReturningStackDestSlot(Slot slot) {
//        setPrivateValue(GuiContainer.class, this, slot, "field_85047_t", "returningStackDestSlot",
//                "x");
//    }
//    
//    protected int getField_85049_r() {
//        return getPrivateValue(GuiContainer.class, this, "field_85049_r", "v");
//    }
//    
//    protected void setField_85049_r(int i) {
//        setPrivateValue(GuiContainer.class, this, i, "field_85049_r", "v");
//    }
//    
//    protected int getField_85048_s() {
//        return getPrivateValue(GuiContainer.class, this, "field_85048_s", "w");
//    }
//    
//    protected void setField_85048_s(int i) {
//        setPrivateValue(GuiContainer.class, this, i, "field_85048_s", "w");
//    }
//}
