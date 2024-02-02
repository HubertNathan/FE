package GameEngine;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    protected boolean rightPressed;
    public boolean leftPressed;
    protected boolean upPressed;
    protected boolean downPressed;
    protected boolean enterPressed;
    protected boolean escapePressed;
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                upPressed = true;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;
            case KeyEvent.VK_RIGHT :
                rightPressed = true;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;
            case KeyEvent.VK_ESCAPE:
                escapePressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                upPressed = false;
                break;
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;
            case KeyEvent.VK_RIGHT :
                rightPressed = false;
                break;
            case KeyEvent.VK_ENTER:
                enterPressed = false;
                break;
            case KeyEvent.VK_ESCAPE:
                escapePressed = false;
                break;
        }
    }
    public  boolean isUpPressed(){return upPressed;}
    public  boolean isDownPressed(){return downPressed;}
    public boolean isLeftPressed(){return leftPressed;}
    public boolean isRightPressed(){return rightPressed;}
    public boolean isEnterPressed(){return enterPressed;}
    public boolean isEscapePressed(){return escapePressed;}

}
