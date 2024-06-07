import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName()
}

actual fun getPlatform(): Platform = IOSPlatform()