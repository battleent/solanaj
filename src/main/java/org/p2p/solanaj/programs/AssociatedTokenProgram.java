package org.p2p.solanaj.programs;

import org.p2p.solanaj.core.AccountMeta;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.Sysvar;
import org.p2p.solanaj.core.TransactionInstruction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AssociatedTokenProgram extends Program {

    public static final PublicKey PROGRAM_ID = new PublicKey("ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL");

    public static TransactionInstruction associatedTokenCreate(PublicKey fundingAddress, PublicKey walletAddress, PublicKey tokenMintAddress, PublicKey associatedAccountAddress) {
        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(fundingAddress, true, true));
        keys.add(new AccountMeta(associatedAccountAddress, false, true));
        keys.add(new AccountMeta(walletAddress, false, false));
        keys.add(new AccountMeta(tokenMintAddress, false, false));
        keys.add(new AccountMeta(SystemProgram.PROGRAM_ID, false, false));
        keys.add(new AccountMeta(TokenProgram.PROGRAM_ID, false, false));
        keys.add(new AccountMeta(Sysvar.SYSVAR_RENT_ADDRESS, false, false));

        return createTransactionInstruction(
                PROGRAM_ID,
                keys,
                new byte[]{}
        );
    }

    public static PublicKey findProgramAddress(PublicKey walletAddress, PublicKey tokenMintAddress) throws Exception {
        return PublicKey.findProgramAddress(
                Arrays.asList(
                        walletAddress.toByteArray(),
                        TokenProgram.PROGRAM_ID.toByteArray(),
                        tokenMintAddress.toByteArray()
                ), PROGRAM_ID
        ).getAddress();
    }
}
