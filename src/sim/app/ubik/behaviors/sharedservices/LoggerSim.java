/*
* 
* 
* This file is part of VoteSim. VoteSim is a UbikSim library. 
* 
* VoteSim has been developed by members of the research Group on 
* Intelligent Systems [GSI] (Grupo de Sistemas Inteligentes), 
* acknowledged group by the  Technical University of Madrid [UPM] 
* (Universidad Politécnica de Madrid) 
* 
* Authors:
* Mercedes Garijo
* Carlos A. Iglesias
* Pablo Moncada
* Emilio Serrano
* 
* Contact: 
* http://www.gsi.dit.upm.es/;
* 
* 
* 
* VoteSim, as UbikSim, is free software: 
* you can redistribute it and/or modify it under the terms of the GNU 
* General Public License as published by the Free Software Foundation, 
* either version 3 of the License, or (at your option) any later version. 
*
* 
* VoteSim is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with VoteSim. If not, see <http://www.gnu.org/licenses/>
 */

package sim.app.ubik.behaviors.sharedservices;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;


public class LoggerSim {
	
	private Level level;
	
	public LoggerSim(Level level) {
		this.level = level;
	}
	
	public LoggerSim() {
		Properties prop = new Properties();		 
    	try {
    		prop.load(new FileInputStream("logger.properties"));    		
    		this.level = Level.parse(prop.getProperty("level"));
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        }
	}
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public void info(String msg) {
		if(level.INFO.intValue() >= this.level.intValue())
			System.out.println(msg);		
	}
	
	public void fine(String msg) {
		if(level.FINE.intValue() >= this.level.intValue())
			System.out.println(msg);		
	}
	
	public void finest(String msg) {
		if(level.FINEST.intValue() >= this.level.intValue())
			System.out.println(msg);		
	}
	
	public void warning(String msg) {
		if(level.WARNING.intValue() >= this.level.intValue())
			System.out.println(msg);		
	}
	public void config(String msg) {
		if(level.CONFIG.intValue() >= this.level.intValue())
			System.out.println(msg);		
	}

}
