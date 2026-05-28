package iteration2.ui.tests;

import iteration2.ui.extensions.UiSession;
import iteration2.ui.extensions.UserSession;
import iteration2.ui.extensions.UserSessionExtension;
import iteration2.ui.utils.UiTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import specs.ResponseSpecs;

import static iteration2.ui.utils.UiAssertions.assertAlertContainsAndAccept;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(UserSessionExtension.class)
public class ChangeProfileNameTest extends BaseUiTest {

    @Test
    public void userCanChangeProfileNameToValidNameTest(@UserSession UiSession session) {
        String expectedName = UiTestData.validProfileName();

        loginAsUser(session);

        customerUiSteps.openEditProfilePage();
        customerUiSteps.changeProfileName(expectedName);

        assertAlertContainsAndAccept(ResponseSpecs.PROFILE_UPDATED_UI_MESSAGE);

        assertThat(uiApiBridge.getCustomerProfile(session.getUser()).getName())
                .isEqualTo(expectedName);
    }

    @Test
    public void userCanNotChangeProfileNameWithInvalidFormatTest(@UserSession UiSession session) {
        String nameBeforeUpdate = uiApiBridge.getCustomerProfile(session.getUser()).getName();

        loginAsUser(session);

        customerUiSteps.openEditProfilePage();
        customerUiSteps.changeProfileName(UiTestData.INVALID_PROFILE_NAME);

        assertAlertContainsAndAccept(ResponseSpecs.INVALID_PROFILE_NAME_API_MESSAGE);
        assertThat(uiApiBridge.getCustomerProfile(session.getUser()).getName())
                .isEqualTo(nameBeforeUpdate);
    }
}