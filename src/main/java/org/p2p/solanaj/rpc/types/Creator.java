package org.p2p.solanaj.rpc.types;

import org.p2p.solanaj.core.PublicKey;

public class Creator {

    public PublicKey publicKey;
    public Boolean verified;
    public int share;

    public Creator(PublicKey publicKey, Boolean verified, int share) {
        this.publicKey = publicKey;
        this.verified = verified;
        this.share = share;
    }
}
