package squeek.squeedometer.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import squeek.squeedometer.config.ConfigWrapper;
import squeek.squeedometer.config.SqueedometerConfig.Position;

@Environment(EnvType.CLIENT)
public class SqueedometerHud {
    
    // Vars
    private MinecraftClient client;
    private TextRenderer textRenderer;

    private int color = ConfigWrapper.config.textColor;
    private int vertColor = ConfigWrapper.config.textColor;
    private double lastFrameSpeed = 0.0;
    private double lastFrameVertSpeed = 0.0;
    private float tickCounter = 0.0f;


    public void draw(DrawContext context, float tickDelta) {
        this.client = MinecraftClient.getInstance();
        this.textRenderer = client.textRenderer;

        // Calculating Speed
        Vec3d playerPosVec = client.player.getPos();
        double travelledX = playerPosVec.x - client.player.prevX;
        double travelledZ = playerPosVec.z - client.player.prevZ;
        double currentSpeed = (double)MathHelper.sqrt((float)(travelledX * travelledX + travelledZ * travelledZ));
        double currentVertSpeed = playerPosVec.y - client.player.prevY;

        if (ConfigWrapper.config.changeColors) {
            // Every tick determine if speeds are increasing or decreasing and set color accordingly   
            tickCounter += tickDelta;
            if (tickCounter >= (float)ConfigWrapper.config.tickInterval) {
                if (currentSpeed < lastFrameSpeed) {
                    color = ConfigWrapper.config.deceleratingColor;
                } else if (currentSpeed > lastFrameSpeed) {
                    color = ConfigWrapper.config.acceleratingColor;
                } else {
                    color = ConfigWrapper.config.textColor;
                }

                if (currentVertSpeed < lastFrameVertSpeed) {
                    vertColor = ConfigWrapper.config.deceleratingColor;
                } else if (currentVertSpeed > lastFrameVertSpeed) {
                    vertColor = ConfigWrapper.config.acceleratingColor;
                } else {
                    vertColor = ConfigWrapper.config.textColor;
                }

                lastFrameSpeed = currentSpeed;
                lastFrameVertSpeed = currentVertSpeed;
                tickCounter = 0.0f;
            }
        }

        String currentVertSpeedText = "";
        String currentSpeedText = "";
        // Convert speeds to text
        if (ConfigWrapper.config.showVertical) {
            currentVertSpeedText = String.format("Vertical: %s", SpeedCalculator.speedText(currentVertSpeed, ConfigWrapper.config.speedUnit));
            currentSpeedText = String.format("Horizontal: %s", SpeedCalculator.speedText(currentSpeed, ConfigWrapper.config.speedUnit));
        } else {
            currentSpeedText = SpeedCalculator.speedText(currentSpeed, ConfigWrapper.config.speedUnit);
        }
        // Calculate text position
        int horizWidth = this.textRenderer.getWidth(currentSpeedText);
        int vertWidth = this.textRenderer.getWidth(currentVertSpeedText);
        int height = this.textRenderer.fontHeight;
        int paddingX = 2;
        int paddingY = 2;
        int marginX = 4;
        int marginY = 4;
        int left = 0 + marginX;
        int vertLeft = 0 + marginX;
        int top = 0 + marginY;
        int realHorizWidth = horizWidth + paddingX * 2 - 1;
        int realVertWidth = vertWidth + paddingX * 2 - 1;
        int realHeight = height + paddingY * 2 - 1;

        if (ConfigWrapper.config.position == Position.BOTTOM_LEFT) {
            top += client.getWindow().getScaledHeight() - marginY * 2 - realHeight;

            left += paddingX;
            vertLeft += paddingX;
            top += paddingY;
        }

        if (ConfigWrapper.config.position == Position.BOTTOM_RIGHT) {
            top += client.getWindow().getScaledHeight() - marginY * 2 - realHeight;
            left += client.getWindow().getScaledWidth() - marginX * 2 - realHorizWidth;
            vertLeft += client.getWindow().getScaledWidth() - marginX * 2 - realVertWidth;

            left += paddingX;
            vertLeft += paddingX;
            top += paddingY;
        }

        if (ConfigWrapper.config.position == Position.TOP_LEFT) {
            left += paddingX;
            vertLeft += paddingX;
            top += paddingY;

            if (ConfigWrapper.config.showVertical) {
                top += 10;
            }
        }

        if (ConfigWrapper.config.position == Position.TOP_RIGHT) {
            left += client.getWindow().getScaledWidth() - marginX * 2 - realHorizWidth;
            vertLeft += client.getWindow().getScaledWidth() - marginX * 2 - realVertWidth;

            left += paddingX;
            vertLeft += paddingX;
            top += paddingY;

            if (ConfigWrapper.config.showVertical) {
                top += 10;
            }
        }

        // Render the text
        context.drawTextWithShadow(this.textRenderer, currentVertSpeedText, vertLeft, top - 10, vertColor);
        context.drawTextWithShadow(this.textRenderer, currentSpeedText, left, top, color);

        return;
    }
}
