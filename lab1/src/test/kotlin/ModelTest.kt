import org.example.task3.bodyparts.*
import org.example.task3.exception.*
import org.example.task3.interfaces.Surface
import org.example.task3.model.ControlPanel
import org.example.task3.model.Person
import org.example.task3.utils.InteractionAction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ModelTest {


    private lateinit var person: Person
    private lateinit var personWithLegsOnSurface: Person

    @BeforeEach
    fun setupPerson() {
        person = Person("Name")
        person.addBodyPart(
            Leg(Sides.LEFT), Leg(Sides.RIGHT),
            Arm(Sides.LEFT), Arm(Sides.RIGHT),
            Head("Left"), Head("Right")
        )

    }

    @BeforeEach
    fun setupPersonWithLegsOnSurface() {
        personWithLegsOnSurface = Person("Name")
        personWithLegsOnSurface.addBodyPart(
            Leg(Sides.LEFT), Leg(Sides.RIGHT),
            Arm(Sides.LEFT), Arm(Sides.RIGHT),
            Head("Left"), Head("Right")
        )

        val legs = personWithLegsOnSurface.findBodyPart(Leg::class)
        val s: Surface = ControlPanel()
        legs.forEach { x ->
            x as Leg
            x.placeOn(s)
        }
    }



    @Test
    fun addBodyPartFunctionTest() {
        val bp = Head("h1")
        bp.addFunction("Test function", InteractionAction("Test function") { _, _ ->
            println("Test")
        })
        assertTrue(bp.getBodyPartSeparateFunctionsSize() > 0)
    }

    @Test
    fun removeBodyPartFunctionTest() {
        val bp = Head("h1")
        bp.addFunction("Test function", InteractionAction("Test function") { _, _ ->
            println("Test")
        })
        val bpSize = bp.getBodyPartSeparateFunctionsSize()
        bp.removeFunction("Test function")
        assertEquals(bp.getBodyPartSeparateFunctionsSize(), bpSize - 1)
    }

    @Test
    fun testExecutionFunctionByName() {
        val bp = Head("h1")
        bp.addFunction("Test function", InteractionAction("Test function") { _, _ ->
            println("Test")
        })

        assertDoesNotThrow { bp.executeFunctionByName("Test function", bp) }
    }

    @Test
    fun bodyPartFunctionNotFoundTest() {
        val bp = Head("h1")
        bp.addFunction("Test function", InteractionAction("Test function") { _, _ ->
            println("Test")
        })

        assertThrows<FunctionDoesNotExist> { bp.executeFunctionByName("Test", bp) }
    }

    @Test
    fun armManipulationTest() {
        val arm = Arm(Sides.LEFT)
        val head = Head("h1")
        arm.pick(head.jaw.teeth)

        assertEquals(true, head.isBusy)
        assertEquals(true, head.jaw.isBusy)
        assertEquals(true, head.jaw.teeth.isBusy)
        assertEquals(true, head.jaw.teeth.isBeingPicked)
    }

    @Test
    fun smileTest() {
        val head = Head("h2")

        head.smile(SmileType.SMILE)

        assertEquals(true, head.isBusy)
        assertEquals(true, head.jaw.isBusy)
        assertEquals(SmileType.SMILE, head.jaw.smileType)
    }

    @Test
    fun bodyPartIsBusySmilingTest() {
        val head = Head("h2")
        val arm = Arm(Sides.LEFT)

        head.smile(SmileType.SMILE)

        assertThrows<BodyPartIsBusyException> { arm.pick(head.jaw.teeth) }
    }

    @Test
    fun bodyPartIsBusyPickingTest(){
        val head = Head("h2")
        val arm = Arm(Sides.LEFT)

        arm.pick(head.jaw.teeth)

        assertThrows<BodyPartIsBusyException> { head.smile(SmileType.SMILE) }
    }

    @Test
    fun putLegsOnFreeSurfaceTest() {
        val s: Surface = ControlPanel()
        val leg = Leg(Sides.LEFT)
        leg.placeOn(s)
        assertNotNull(leg.isOnSurface)
    }

    @Test
    fun surfaceBusyTest() {
        val s: Surface = ControlPanel(isAvailableSurface = false)
        val leg = Leg(Sides.LEFT)
        assertThrows<SurfaceIsBusyException> { leg.placeOn(s) }

    }

    @Test
    fun shockTest() {
        personWithLegsOnSurface.shock()

        assertEquals(true, personWithLegsOnSurface.getEmotions().calcShockLevel() > 20)
    }

    @Test
    fun noShockEventHappenedTest() {
        assertThrows<NoShockingEventOccurredException> { person.shock() }
    }

    @Test
    fun jawDropTest() {


        personWithLegsOnSurface.shock()

        personWithLegsOnSurface.jawDrop()

        val jaws = personWithLegsOnSurface.findBodyPart(Jaw::class)

        val jawsDropped = jaws.all { x ->
            x as Head
            x.jaw.isDropped
        }

        assertEquals(true, jawsDropped)

    }

    @Test
    fun jawCantBeDroppedTest() {
        assertThrows<NoShockingEventOccurredException> { person.jawDrop() }
    }

    @Test
    fun removeLegsFromSurfaceTest(){
        val p = personWithLegsOnSurface
        val legs = p.findBodyPart(Leg::class)

        legs.forEach { x ->
            x as Leg
            x.removeFromSurface()
        }

        legs.forEach { x -> x as Leg
        assertNull(x.isOnSurface)}
    }

    @Test
    fun jawAlreadyDroppedTest(){
        val person = personWithLegsOnSurface
        person.shock()
        person.jawDrop()

        assertThrows<JawAlreadyDroppedException> { person.jawDrop() }
    }

    @Test
    fun jawAlreadyClosedTest(){
        val person = personWithLegsOnSurface
        person.shock()
        person.jawDrop()

        val heads = person.findBodyPart(Head::class)

        heads.forEach { x ->
            x as Head
            x.jaw.closeJaw()
        }
        heads.forEach { x ->
            x as Head
            assertThrows<JawClosedException> { x.jaw.closeJaw() }
        }
    }
}