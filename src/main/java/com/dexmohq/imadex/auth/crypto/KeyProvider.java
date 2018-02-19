package com.dexmohq.imadex.auth.crypto;

import java.security.Key;

interface KeyProvider {
    Key getKey();
}
