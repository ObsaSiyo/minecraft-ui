package local.minecraftfrontend.interfaces;

import io.wispforest.owo.ui.component.ButtonComponent;
import io.wispforest.owo.ui.container.FlowLayout;
import io.wispforest.owo.ui.core.Component;
import io.wispforest.owo.ui.core.Sizing;
import local.minecraftfrontend.pages.Form;

/** Transactional family entry point built from a reusable Form page; no transaction logic. */
public final class TransactionalForm {
    private TransactionalForm() {}
    public static FlowLayout create(Sizing width, Sizing height, Component fields, Component feedback,
                                    ButtonComponent submit, ButtonComponent cancel) {
        return Form.create(width, height, fields, feedback, submit, cancel);
    }
}
