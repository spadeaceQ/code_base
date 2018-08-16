package micromanager;

import mmcorej.CMMCore;
import mmcorej.Configuration;
import mmcorej.PropertySetting;
import mmcorej.StrVector;


public class Pvcam {
   
   public static void main(String[] args) throws Exception {
      
      // create core object
      CMMCore core = new CMMCore();
      
     

      try {
         
     
    	  core.loadSystemConfiguration("pvcam.cfg");
    	  
         // list devices
         StrVector devices = core.getLoadedDevices();
         System.out.println("Device status:");
         
         for (int i=0; i<devices.size(); i++){
            System.out.println(devices.get(i)); 
            // list device properties
            StrVector properties = core.getDevicePropertyNames(devices.get(i));
            if (properties.size() == 0)
               System.out.println("   No properties.");
            for (int j=0; j<properties.size(); j++){
               System.out.println("   " + properties.get(j) + " = "
                     + core.getProperty(devices.get(i), properties.get(j)));
               StrVector values = core.getAllowedPropertyValues(devices.get(i), properties.get(j));
               for (int k=0; k<values.size(); k++){
                  System.out.println("      " + values.get(k));
               }
            }
         }
         
         // list configurations
         StrVector groups = core.getAvailableConfigGroups();
         for (int i=0; i<groups.size(); i++) {
            StrVector configs = core.getAvailableConfigs(groups.get(i));
            System.out.println("Group " + groups.get(i));
            for (int j=0; j<configs.size(); j++) {
               Configuration cdata = core.getConfigData(groups.get(i), configs.get(j));
               System.out.println("   Configuration " + configs.get(j));
               for (int k=0; k<cdata.size(); k++) {
                  PropertySetting s = cdata.getSetting(k);
                  System.out.println("      " + s.getDeviceLabel() + ", " +
                                     s.getPropertyName() + ", " + s.getPropertyValue());
               }
            }
      }
         
      } catch (Exception e) {
         System.out.println(e.getMessage());
         if(e.getMessage().contains("DDI_SYS_ERR_SEND_BYTE")){
        	 System.out.println("Camera not detected, please recheck camera connections!!");
         }
         System.exit(1);
      }      
  }
}