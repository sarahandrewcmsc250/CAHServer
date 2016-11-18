package cah;

import cahserver.Lobby;

public interface CAHConstants {
    public static int DRAW_WHITE = 1;
    public static int PLAY_WHITE = 2;
    public static int PICK_WHITE = 3;
    public static int GET_HAND = 11;
    public static int GET_LOBBIES = 4;
    public static int SEND_LOBBY = 5;
    public static int SEND_HANDLE = 6;
    public static int READY_UP = 7;
    public static int UNREADY = 8;
    public static int GET_SCORE = 9;
    public static int GET_ALL_SCORES = 14;
    public static int DRAW_BLACK = 10;
    public static int GET_BLACK = 12;
    public static int GET_CZAR = 13;
    public static int GET_TO_JUDGE = 15;
    public static Lobby LOBBY_1 = new Lobby(1);
    public static Lobby LOBBY_2 = new Lobby(2);
    public static Lobby LOBBY_3 = new Lobby(3);
    public static Lobby LOBBY_4 = new Lobby(4);
    public static Lobby LOBBY_5 = new Lobby(5);
}
