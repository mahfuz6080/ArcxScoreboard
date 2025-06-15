public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        String licenseKey = getConfig().getString("license");
        if (!checkLicense(licenseKey)) {
            getLogger().severe("Invalid license! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("License validated. Plugin enabled.");
    }

    private boolean checkLicense(String key) {
        try {
            URL url = new URL("https://yourdomain.com/license.php?key=" + key);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String response = in.readLine();
            in.close();
            return "VALID".equalsIgnoreCase(response);
        } catch (Exception e) {
            getLogger().severe("Could not contact license server: " + e.getMessage());
            return false;
        }
    }
}

