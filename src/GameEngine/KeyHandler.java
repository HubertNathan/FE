package GameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    protected boolean rightPressed;
    protected boolean leftPressed;
    protected boolean upPressed;
    protected boolean downPressed;
    protected boolean enterPressed;
    protected boolean escapePressed;
    protected boolean arrowPressed;
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                upPressed = true;
                arrowPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                arrowPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                arrowPressed = true;
                break;
            case KeyEvent.VK_RIGHT :
                rightPressed = true;
                arrowPressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                arrowPressed = true;
                break;
            case KeyEvent.VK_ESCAPE:
                escapePressed = true;
                arrowPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                upPressed = false;
                arrowPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                arrowPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                arrowPressed = false;
                break;
            case KeyEvent.VK_RIGHT :
                rightPressed = false;
                arrowPressed = false;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = false;
                arrowPressed = false;
                break;
            case KeyEvent.VK_ESCAPE:
                escapePressed = false;
                arrowPressed = false;
                break;
        }
    }
    public  boolean isUpPressed(){return upPressed;}
    public  boolean isDownPressed(){return downPressed;}
    public boolean isLeftPressed(){return leftPressed;}
    public boolean isRightPressed(){return rightPressed;}
    public boolean isEnterPressed(){return enterPressed;}
    public boolean isEscapePressed(){return escapePressed;}
    public boolean isArrowPressed() {return arrowPressed;}
}
