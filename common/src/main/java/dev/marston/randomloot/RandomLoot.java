package dev.marston.randomloot;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class RandomLoot {
    public static final String MODID = "randomloot";
    public static final Logger LOGGER = LogUtils.getLogger();

    // We can use this if we don't want to use DeferredRegister
//    public static final Supplier<RegistrarManager> REGISTRIES = Suppliers.memoize(() -> RegistrarManager.get(MOD_ID));
//
//    // Registering a new creative tab
//    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(MOD_ID, Registries.CREATIVE_MODE_TAB);
//    public static final RegistrySupplier<CreativeModeTab> EXAMPLE_TAB = TABS.register("example_tab", () ->
//            CreativeTabRegistry.create(Component.translatable("itemGroup." + MOD_ID + ".example_tab"),
//                    () -> new ItemStack(RandomLoot.EXAMPLE_ITEM.get())));
//
//    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registries.ITEM);
//    public static final RegistrySupplier<Item> EXAMPLE_ITEM = ITEMS.register("example_item", () ->
//            new Item(new Item.Properties().arch$tab(RandomLoot.EXAMPLE_TAB)));
    
    public static void init() {
//        TABS.register();
//        ITEMS.register();
        
        System.out.println(ExpectPlatform.getConfigDirectory().toAbsolutePath().normalize().toString());
    }
}
