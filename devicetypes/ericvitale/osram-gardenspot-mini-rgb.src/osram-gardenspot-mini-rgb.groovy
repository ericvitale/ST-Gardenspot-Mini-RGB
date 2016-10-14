/**
  * OSRAM Gardenspot Mini RGB
  *
  *
  * Version 1.0.2 - Added support for toggle, setFlash, setBreath. - 10/14/2016
  * Version 1.0.1 - Added command for randomHue to be called by CoRE. - 10/13/2016
  * Version 1.0.0 - Initial Release - 10/13/2016
  *
  * Licensed under the Apache License, Version 2.0 (the "License"); you may not
  * use this file except in compliance with the License. You may obtain a copy
  * of the License at:
  *
  * http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
  * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
  * License for the specific language governing permissions and limitations
  * under the License.
  *
  * You can find this device handler @ https://github.com/ericvitale/ST-Gardenspot-Mini-RGB
  * You can find my other device handlers & SmartApps @ https://github.com/ericvitale
  *
  *
**/

def getVersion() { return "1.0.1" }

metadata {
	definition (name: "OSRAM GardenSpot Mini RGB", namespace: "ericvitale", author: "ericvitale@gmail.com") {
		capability "Switch Level"
		capability "Actuator"
		capability "Color Control"
        capability "Color Temperature"
		capability "Switch"
		capability "Configuration"
		capability "Polling"
		capability "Refresh"
		capability "Sensor"
        
        attribute "hue", "number"
        attribute "saturation", "number"
        attribute "hueLoop", "string"
        attribute "loopSpeed", "number"
        attribute "breathe", "string"
        attribute "flash", "string"
        
        command "randomHue"
        command "setHueLoop"
        command "setFlash"
        command "setBreathe"
        command "toggle"
        command "setLevel", ["number"]
        command "setLevel", ["number", "number"]

		fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0006,0008,0300,0B04,FC0F", outClusters: "0019", manufacturer: "OSRAM", model: "Gardenspot RGB"
		fingerprint profileId: "0104", inClusters: "0000,0003,0004,0005,0006,0008,0300,0B04,FC0F", outClusters: "0019", manufacturer: "OSRAM", model: "LIGHTIFY Gardenspot RGB"
	}
    
    preferences {       
       	section("Settings") {
	        input "logging", "enum", title: "Log Level", required: false, defaultValue: "INFO", options: ["TRACE", "DEBUG", "INFO", "WARN", "ERROR"]	
        }
    }
    
    tiles(scale: 2) {
    	multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"st.Lighting.light14.on", backgroundColor:"#79b821"//, nextState:"turningOff"
				attributeState "off", label:'${name}', action:"switch.on", icon:"st.Lighting.light14.off", backgroundColor:"#ffffff"//, nextState:"turningOn"
			}
            
            tileAttribute ("device.level", key: "SECONDARY_CONTROL") {
				attributeState "default", label:'${currentValue}%'
			}
            
            tileAttribute ("device.level", key: "SLIDER_CONTROL") {
        		attributeState "default", action:"switch level.setLevel"
            }
        }
        
        multiAttributeTile(name:"switchDetails", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label:'${name}', action:"switch.off", icon:"http://hosted.lifx.co/smartthings/v1/196xOn.png", backgroundColor:"#79b821"
				attributeState "off", label:'${name}', action:"switch.on", icon:"http://hosted.lifx.co/smartthings/v1/196xOff.png", backgroundColor:"#ffffff"
			}
            
            tileAttribute ("device.lastActivity", key: "SECONDARY_CONTROL") {
				attributeState "default", label:'Last activity: ${currentValue}', action: "refresh.refresh"
			}
        }
        
        valueTile("Brightness", "device.level", width: 2, height: 1) {
        	state "level", label: 'Brightness ${currentValue}%'
        }
        
        valueTile("Hue", "device.hue", width: 2, height: 1) {
        	state "hue", label: 'Hue ${currentValue}'
        }
        
        valueTile("Saturation", "device.saturation", width: 2, height: 1) {
        	state "saturation", label: 'Sat ${currentValue}%'
        }
        
        controlTile("satSliderControl", "device.saturation", "slider", height: 1, width: 4, inactiveLabel: false, range:"(0..100)") {
			state "saturation", action:"setSaturation"
		}
        
        controlTile("levelSliderControl", "device.level", "slider", width: 4, height: 1) {
        	state "level", action:"switch level.setLevel"
        }
        
        controlTile("hueSliderControl", "device.hue", "slider", height: 1, width: 4, inactiveLabel: false, range:"(0..100)") {
			state "hue", action:"setHue"
		}
        
        controlTile("rgbSelector", "device.color", "color", height: 3, width: 3, inactiveLabel: false) {
            state "color", action:"setColor"
        }
        
        standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat", height: 3, width: 3) {
			state "default", label:"", action:"refresh.refresh", icon: "st.secondary.refresh"
		}
        
        standardTile("hueLoop", "device.hueLoop", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
        	state "on", label: 'Hue Loop On', action: "setHueLoop", backgroundColor: "#ab1552", nextState: "off"
			state "off", label:'Hue Loop Off', action: "setHueLoop", backgroundColor: "#ffffff", nextState: "on"
        }
        
        standardTile("breathe", "device.breathe", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
        	state "on", label: 'Breathe On', action: "setBreathe", backgroundColor: "#ab1552", nextState: "off"
			state "off", label:'Breathe Off', action: "setBreathe", backgroundColor: "#ffffff", nextState: "on"
        }
        
        standardTile("flash", "device.flash", height: 2, width: 2, inactiveLabel: false, decoration: "flat") {
        	state "on", label: 'Flash On', action: "setFlash", backgroundColor: "#ab1552", nextState: "off"
			state "off", label:'Flash Off', action: "setFlash", backgroundColor: "#ffffff", nextState: "on"
        }

        main(["switch"])
        details(["switchDetails", "Brightness", "levelSliderControl", "Hue", "hueSliderControl", "Saturation", "satSliderControl", "rgbSelector", "refresh", "hueLoop", "flash", "breathe"])
    }
}

private getATTRIBUTE_HUE() { 0x0000 }
private getATTRIBUTE_SATURATION() { 0x0001 }
private getHUE_COMMAND() { 0x00 }
private getSATURATION_COMMAND() { 0x03 }
private getCOLOR_CONTROL_CLUSTER() { 0x0300 }
private getATTRIBUTE_COLOR_TEMPERATURE() { 0x0007 }

private determineLogLevel(data) {
    switch (data?.toUpperCase()) {
        case "TRACE":
            return 0
            break
        case "DEBUG":
            return 1
            break
        case "INFO":
            return 2
            break
        case "WARN":
            return 3
            break
        case "ERROR":
        	return 4
            break
        default:
            return 1
    }
}

def log(data, type) {
    data = "OGP - ${getVersion()} -- ${device.label} -- ${data ?: ''}"
        
    if (determineLogLevel(type) >= determineLogLevel(settings?.logging ?: "INFO")) {
        switch (type?.toUpperCase()) {
            case "TRACE":
                log.trace "${data}"
                break
            case "DEBUG":
                log.debug "${data}"
                break
            case "INFO":
                log.info "${data}"
                break
            case "WARN":
                log.warn "${data}"
                break
            case "ERROR":
                log.error "${data}"
                break
            default:
                log.error "OGP - ${getVersion()} -- ${device.label} -- Invalid Log Setting"
        }
    }
}

def updated() {
	log("Device Updated.", "DEBUG")
    
    log("----Settings----", "INFO")
    log("Logging Level = ${logging}.", "INFO")
    
    if(getStateVersion() < getNextStateVersion() ) {
    	configure()
    }
  
    log("Device Update Complete.", "DEBUG")
}

def poll() {
	log("Poll Initiated.", "DEBUG")
	
    log(getStateVersionString(), "DEBUG")
    
    if(getStateVersion() < getNextStateVersion() ) {
    	configure()
    }
    log("Poll Complete.", "DEBUG")
}

def installed() {
	log("Device Installed.", "DEBUG")
    state.configured = false
    log("Installation Complete.", "DEBUG")
}

def configure() {
	log("Device Configuring...", "DEBUG")
    
    setStateVersion(getNextStateVersion())
    
    def retVal = 
    	zigbee.configureReporting(0x0008, 0x0000, 0x20, 1, 60, 0x01) +
        zigbee.configureReporting(0x0006, 0x0000, 0x10, 0, 600, null) +
        zigbee.configureReporting(0x0300, 0x0000, 0x21, 0, 600, 0x10) +
        zigbee.configureReporting(0x0300, 0x0001, 0x21, 0, 600, 0x10)
            
	return retVal
}

def refresh() {
	log(getStateVersionString(), "DEBUG")
    
    if(getStateVersion() < getNextStateVersion() ) {
    	return configure()
    } else {
		def retVal = zigbee.readAttribute(0x0008, 0x0000) +
    		zigbee.readAttribute(0x0006, 0x0000)
			return retVal
    }
}

def parse(String description) {
	log("parse.description = ${description}", "DEBUG")
    
    if (description?.startsWith("catchall:")) {
        
    } else if(description?.trim().endsWith("on/off: 0")) {
    	log("Lights are Off.", "INFO")
    } else if(description?.trim().endsWith("on/off: 1")) {
    	log("Lights are On.", "INFO")
    } else if (description?.startsWith("read attr -")) {
        def descMap = parseDescriptionAsMap(description)
        
        log("parse.descMap.value = ${descMap.value}.", "DEBUG")
        log("parse.descMap.attrId = ${descMap.attrId}.", "DEBUG")
        log("parse.descMap.cluster = ${descMap.cluster}.", "DEBUG")
        
        if(descMap?.cluster == "0008") {
    		def lev = Math.round(zigbee.convertHexToInt(descMap.value) * 100 / 255)
            log("Lights Level is ${lev}.", "INFO")
    	} else if(descMap?.cluster == "0300") {
        	log("Color is being set", "INFO")
            
            if(descMap.attrId == "0000") {	
            	def hue = Math.round(zigbee.convertHexToInt(descMap.value) * 100 / 255)
                log("Hue is set to ${hue}.", "INFO")
                sendEvent(name: "hue", value: hue, displayed:false)
            } else if(descMap.attrId == "0001") {
	            def sat = Math.round(zigbee.convertHexToInt(descMap.value) * 100 / 255)
                log("Saturation is set to ${sat}.", "INFO")
                sendEvent(name: "saturation", value: sat, displayed:false)
            }
        }
    }    
}

def parseDescriptionAsMap(description) {
    (description - "read attr - ").split(",").inject([:]) { map, param ->
        def nameAndValue = param.split(":")
        map += [(nameAndValue[0].trim()):nameAndValue[1].trim()]
    }
}

def on() {
	log("Turning on.", "DEBUG")
    sendEvent(name: "switch", value: "on")
    zigbee.on()
}

def off() {
	log("Turning off.", "DEBUG")
    sendEvent(name: "switch", value: "off")
    zigbee.off()
}

def setLevel(level) {
	def brightness = level
    log("Setting level to ${brightness}.", "DEBUG")
    
    sendEvent(name: "level", value: brightness)
    sendEvent(name: "switch", value: "on")
    zigbee.setLevel(brightness, 10)
}

def setLevel(level, duration) {
	log("setLevel(${level}) with duration(${duration}).", "DEBUG")
    sendEvent(name: "level", value: brightness)
    sendEvent(name: "switch", value: "on")
    zigbee.setLevel(brightness, duration * 100)
}

def setColor(value) {
	log("setColor.value = ${value}.", "DEBUG")
    log("setColor.value.hue = ${value.hue}.", "DEBUG")
    log("setColor.value.saturation = ${value.saturation}.", "DEBUG")
	zigbee.on() + setHue(value.hue) + "delay 300" + setSaturation(value.saturation)
}

def setHue(value) {
    def val = zigbee.convertToHexString(Math.round(value * 0xfe / 100.0), 2)
    zigbee.command(COLOR_CONTROL_CLUSTER, 0x00, val, "00", "0500")
}

def setSaturation(value) {
    def val = zigbee.convertToHexString(Math.round(value * 0xfe / 100.0), 2)
    zigbee.command(COLOR_CONTROL_CLUSTER, 0x03, val, "0500")
}

def randomHue() {
	def rHue = new Random().nextInt(100) + 1
    log("Random Hue is ${rHue}.", "DEBUG")
	setHue(rHue)
}

def setHueLoop() {
	if(device.currentValue("hueLoop") == "off") {
    	log("Turning Hue Loop On.", "INFO")
        setDoHueLoop(true)
        sendEvent(name: "hueLoop", value: "on", displayed:true, isStateChange: true)
    } else {
    	log("Turning Hue Loop Off.", "INFO")
        setDoHueLoop(false)
        sendEvent(name: "hueLoop", value: "off", displayed:true, isStateChange: true)
    }
}

def setFlash() {
	if(device.currentValue("flash") == "off") {
    	log("Turning Flash Loop On.", "INFO")
        sendEvent(name: "flash", value: "on", displayed:true, isStateChange: true)
    } else {
    	log("Turning Flash Loop Off.", "INFO")
        sendEvent(name: "flash", value: "off", displayed:true, isStateChange: true)
    }
}

def setBreathe() {
	if(device.currentValue("breathe") == "off") {
    	log("Turning Breathe Loop On.", "INFO")
        sendEvent(name: "breathe", value: "on", displayed:true, isStateChange: true)
    } else {
    	log("Turning Breathe Loop Off.", "INFO")
        sendEvent(name: "breathe", value: "off", displayed:true, isStateChange: true)
    }
}

def toggle() {
	if(device.currentValue("switch") == "off") {
    	on()
    } else {
    	off()
    }
}

//---------------------------

def getNextStateVersion() {
	return 1
}

def getStateVersion() {
	if(state.version == null) {
    	return 0
    } else {
    	return state.version
    }
}

def setStateVersion(val) {
	state.version = val
}

def getStateVersionString() {
	return "The current state version is ${getStateVersion()} and the next state version is ${getNextStateVersion()}."
}

def isConfigured() {
	return state.configured
}

def shouldHueLoop() {
	if(state.doHueLoop == null) {
    	return false
    } else {
    	return state.doHueLoop
    }
}

def setDoHueLoop(val) {
	state.doHueLoop = val
}