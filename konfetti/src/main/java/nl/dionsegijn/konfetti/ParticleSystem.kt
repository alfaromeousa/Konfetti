package nl.dionsegijn.konfetti

import android.graphics.Canvas
import android.graphics.Color
import android.support.annotation.ColorInt
import nl.dionsegijn.konfetti.emitters.BurstEmitter
import nl.dionsegijn.konfetti.emitters.Emitter
import nl.dionsegijn.konfetti.emitters.StreamEmitter
import nl.dionsegijn.konfetti.models.LocationModule
import nl.dionsegijn.konfetti.models.Shape
import nl.dionsegijn.konfetti.models.Size
import nl.dionsegijn.konfetti.modules.VelocityModule
import java.util.*

/**
 * Created by dionsegijn on 3/26/17.
 */
class ParticleSystem(val konfettiView: KonfettiView) {

    private val random = Random()

    /** Modules */
    private var location = LocationModule(random)
    private var velocity = VelocityModule(random)

    /** Default values */
    private var colors = intArrayOf(Color.RED)
    private var sizes = arrayOf(Size.SMALL)
    private var shapes = arrayOf(Shape.RECT)

    /**
     * Implementation of [BurstEmitter] or [StreamEmitter]
     */
    private lateinit var emitter: Emitter

    /**
     * Set position to emit particles from
     */
    fun setPosition(x: Float, y: Float): ParticleSystem {
        location.setX(x)
        location.setY(y)
        return this
    }

    /**
     * Set position range to emit particles in between
     */
    fun setPosition(minX: Float, maxX: Float? = null, minY: Float, maxY: Float? = null): ParticleSystem {
        location.betweenX(minX, maxX)
        location.betweenY(minY, maxY)
        return this
    }

    /**
     * One of the colors will be randomly picked when confetti is generated
     * Default color is Color.RED
     */
    fun addColors(@ColorInt vararg colors: Int): ParticleSystem {
        this.colors = colors
        return this
    }

    /**
     * Configure one or more sizes predefined in [Size].
     * Default size is [Size.SMALL]
     */
    fun addSizes(vararg possibleSizes: Size): ParticleSystem {
        this.sizes = possibleSizes.filterIsInstance<Size>().toTypedArray()
        return this
    }

    /**
     * Configure one or more shapes predefined in [Shape]
     * Default shape is [Shape.RECT] rectangle
     */
    fun addShapes(vararg shapes: Shape): ParticleSystem {
        this.shapes = shapes.filterIsInstance<Shape>().toTypedArray()
        return this
    }

    /**
     * Set direction you want to have the particles shoot to
     * [direction] direction in degrees ranging from 0 - 360
     * default direction is 0
     */
    fun setDirection(direction: Double): ParticleSystem {
        velocity.minAngle = Math.toRadians(direction)
        return this
    }

    /**
     * Set direction you want to have the particles shoot to
     * [minDirection] direction in degrees
     * [maxDirection] direction in degrees
     * Default minDirection is 0 and maxDirection is by default not set
     */
    fun setDirection(minDirection: Double, maxDirection: Double): ParticleSystem {
        velocity.minAngle = Math.toRadians(minDirection)
        velocity.maxAngle = Math.toRadians(maxDirection)
        return this
    }

    /**
     * Set the speed of the particle
     * If value is negative it will be automatically set to 0
     */
    fun setSpeed(speed: Float): ParticleSystem {
        velocity.minSpeed = speed
        return this
    }

    /**
     * Set the speed range of the particle
     * If one of the values is negative it will be automatically set to 0
     */
    fun setSpeed(minSpeed: Float, maxSpeed: Float): ParticleSystem {
        velocity.minSpeed = minSpeed
        velocity.maxSpeed = maxSpeed
        return this
    }

    fun burst(amount: Int) {
        emitter = BurstEmitter(location, velocity, sizes, shapes, colors).burst(amount)
        start()
    }

    fun emit(particlesPerSecond: Int) {
        emitter = StreamEmitter(location, velocity, sizes, shapes, colors).emit(particlesPerSecond)
        start()
    }

    fun emit(particlesPerSecond: Int, emittingTime: Int) {
        emitter = StreamEmitter(location, velocity, sizes, shapes, colors).emit(particlesPerSecond, emittingTime)
        start()
    }

    fun emit(particlesPerSecond: Int, emittingTime: Int, maxParticles: Int) {
        emitter = StreamEmitter(location, velocity, sizes, shapes, colors).emit(particlesPerSecond, emittingTime, maxParticles)
        start()
    }

    fun start() {
        konfettiView.start(this)
    }

    internal fun render(canvas: Canvas) {
        emitter.render(canvas)
    }

}
