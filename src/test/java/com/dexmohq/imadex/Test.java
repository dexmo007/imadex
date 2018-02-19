package com.dexmohq.imadex;

import java.util.Base64;

public class Test {

    public static void main(String[] args) {
        final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJkZXhtbyIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSJdLCJleHAiOjE1MTkwODMxMjgsInVzZXJJZCI6IjVhOGI0NmZhMzEwNmNlMWVkY2ZlMzM5MyIsImF1dGhvcml0aWVzIjpbIlVTRVIiXSwianRpIjoiMDVmYjU2NTEtNTU5MS00MzdkLWFjZjEtNzA3ZjM4NjA4YmM1IiwiY2xpZW50X2lkIjoiY2k4NzIxODM3MSJ9.X6E3ljD68JZQPfmWvk0eGGdhAiZLw87IJA7T_1lfR0o";
        System.out.println(new String(Base64.getUrlDecoder().decode(token.split("\\.")[1])));

    }
}
