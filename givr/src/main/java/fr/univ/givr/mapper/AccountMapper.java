package fr.univ.givr.mapper;

import fr.univ.givr.model.account.Account;
import fr.univ.givr.model.account.AccountDTO;
import fr.univ.givr.model.account.UserInfo;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper to create instance of account from a dto or data class.
 */
@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AccountMapper {

    /**
     * Create a new instance of account data from the dto instance.
     *
     * @param accountDto Account dto with data verified.
     * @return A new instance of account data.
     */
    @Mapping(target = "verified", constant = "false")
    @Mapping(target = "banned", constant = "false")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Account dtoToAccount(AccountDTO accountDto);

    /**
     * Create a new instance of account dto from the data instance.
     *
     * @param account Account data.
     * @return A new instance of account dto.
     */
    AccountDTO accountToDTO(Account account);

    /**
     * Create a new instance of user info from the data into an account.
     * @param account Account data.
     * @return A new instance containing information about user.
     */
    UserInfo accountToUserInfo(Account account);
}
