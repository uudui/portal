package com.nx.web.controller.dashboard;

import com.sun.management.OperatingSystemMXBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Neal on 12/21 021.
 */
@Controller
@RequestMapping("/dashboard")
public class DashBoard {

    @RequestMapping
    public ModelAndView show() {
        File[] listRoots = File.listRoots();

        //get disc space
        long usableSpace = 0L;
        long totalSpace = 0L;
        for (File listRoot : listRoots) {
            usableSpace += listRoot.getUsableSpace() / 1024 / 1024;
            totalSpace += listRoot.getTotalSpace() / 1024 / 1024;
        }

        //get Memory
        OperatingSystemMXBean osmb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        long totalPhysicalMemorySize = osmb.getTotalPhysicalMemorySize() / 1024 / 1024;
        long freePhysicalMemorySize = osmb.getFreePhysicalMemorySize() / 1024 / 1024;

        Map<String, Long> systemVariable = new HashMap<>();
        systemVariable.put("usableSpace", usableSpace);
        systemVariable.put("totalSpace", totalSpace);
        systemVariable.put("totalPhysicalMemorySize", totalPhysicalMemorySize);
        systemVariable.put("freePhysicalMemorySize", freePhysicalMemorySize);

        return new ModelAndView("dashboard/dashboard", systemVariable);
    }
}
