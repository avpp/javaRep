/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netCardInterfaces;

/**
 *
 * @author Alexey
 */
public class AboutGame {
    private Class<Dealer> _dealerClass;
    private String _gameName;
    private String _aboutGame;
    
    public AboutGame(Class<Dealer> dealerClass, String gameName, String aboutGame) {
        _dealerClass = dealerClass;
        _gameName = gameName;
        _aboutGame = aboutGame;
    }

    /**
     * @return the _dealerClass
     */
    public Class<Dealer> getDealerClass() {
        return _dealerClass;
    }

    /**
     * @return the _gameName
     */
    public String getGameName() {
        return _gameName;
    }

    /**
     * @return the _aboutGame
     */
    public String getAboutGame() {
        return _aboutGame;
    }
    
    @Override
    public String toString() {
        return "className=".concat(_dealerClass.getName()).concat(",name=").concat(_gameName).concat(",about=").concat(_aboutGame);
    }
    
    public boolean isThisGame(String className) {
        return className != null && className.equals(_dealerClass.getName());
    }
}
