package org.p2p.solanaj.programs;

import com.google.common.primitives.Bytes;
import org.p2p.solanaj.core.AccountMeta;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.Sysvar;
import org.p2p.solanaj.core.TransactionInstruction;
import org.p2p.solanaj.rpc.types.Creator;
import org.p2p.solanaj.utils.ByteUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TokenMetadataProgram extends Program {

    public static final PublicKey PROGRAM_ID = new PublicKey("metaqbxxUerdq28cj1RbAWkYQm3ybzjb6a8bt518x1s");

    private static final int CREATE_METADATA_ACCOUNTS_METHOD_ID = 0;

    public static TransactionInstruction createMetadataAccounts(
            PublicKey tokenMintAddress,
            PublicKey mintAuthorityAddress,
            PublicKey payerAddress,
            PublicKey updateAuthorityAddress,
            String name,
            String symbol,
            String uri,
            int fee,
            List<Creator> creators,
            Boolean isMutable
    ) throws Exception {
        PublicKey metadataAccount = PublicKey.findProgramAddress(
                Arrays.asList(
                        "metadata".getBytes(),
                        PROGRAM_ID.toByteArray(),
                        tokenMintAddress.toByteArray()
                ),
                PROGRAM_ID
        ).getAddress();

        final List<AccountMeta> keys = new ArrayList<>();

        keys.add(new AccountMeta(metadataAccount, false, true));
        keys.add(new AccountMeta(tokenMintAddress, true, true));
        keys.add(new AccountMeta(mintAuthorityAddress, true, true));
        keys.add(new AccountMeta(payerAddress, true, true));
        keys.add(new AccountMeta(updateAuthorityAddress, true, true));
        keys.add(new AccountMeta(SystemProgram.PROGRAM_ID, false, false));
        keys.add(new AccountMeta(Sysvar.SYSVAR_RENT_ADDRESS, false, false));

        byte[] data = Bytes.concat(
                new byte[]{(byte) CREATE_METADATA_ACCOUNTS_METHOD_ID},
                ByteUtils.lengthToByteArray(name.getBytes().length),
                name.getBytes(StandardCharsets.UTF_8),
                ByteUtils.lengthToByteArray(symbol.getBytes().length),
                symbol.getBytes(StandardCharsets.UTF_8),
                ByteUtils.lengthToByteArray(uri.getBytes().length),
                uri.getBytes(StandardCharsets.UTF_8),
                ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN).put((byte) fee).array(),
                ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN).put((byte) creators.size()).array()
        );

        for (int i = 0; i < creators.size(); i++) {
            byte verified = (byte) (creators.get(i).verified ? 1 : 0);
            byte share = (byte) creators.get(i).share;
            data = Bytes.concat(
                    data,
                    ByteUtils.lengthToByteArray(i + 1),
                    creators.get(i).publicKey.toByteArray(),
                    ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN).put(verified).array(),
                    ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN).put(share).array()
            );
        }
        data = Bytes.concat(data, ByteBuffer.allocate(1).order(ByteOrder.LITTLE_ENDIAN).put((byte) (isMutable ? 1 : 0)).array());

        return createTransactionInstruction(
                PROGRAM_ID,
                keys,
                data
        );
    }
}
