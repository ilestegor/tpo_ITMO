import org.example.task3.bodyparts.*
import org.example.task3.exception.*
import org.example.task3.interfaces.Surface
import org.example.task3.model.ControlPanel
import org.example.task3.model.EmotionState
import org.example.task3.model.Emotions
import org.example.task3.model.Person
import org.example.task3.utils.InteractionAction
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.lang.reflect.Method
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
        bp.bodyPartSeparateFunctions["Test function"]?.execute(bp)

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

        val jaws = personWithLegsOnSurface.findBodyPart(Head::class)

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

    @Test
    fun testSmileGrinChange(){
        val head = Head("Head")
        head.jaw.smile(SmileType.GRIN, head)

        assertEquals(SmileType.GRIN, head.jaw.smileType)
    }

    @Test
    fun testSadSmileChange(){
        val head = Head("head")
        head.jaw.smile(SmileType.SAD, head)

        assertEquals(SmileType.SAD, head.jaw.smileType)
    }
    @Test
    fun testNoneSmileChange(){
        val head = Head("head")
        head.jaw.smile(SmileType.NONE, head)

        assertEquals(SmileType.NONE, head.jaw.smileType)
    }

    @Test
    fun jawBusyTest(){
        val head = Head("h")
        head.smile(SmileType.SMILE)
        assertThrows<BodyPartIsBusyException> { head.jaw.smile(SmileType.SMILE, head) }
    }


    @Test
    fun testJoyEmotionChange(){
        val emotion = EmotionState()
        emotion.updateEmotion(Emotions.JOY, 5)

        assertEquals(5, emotion.joy)
    }

    @Test
    fun testAngerEmotionChange(){
        val emotion = EmotionState()
        emotion.updateEmotion(Emotions.ANGER, 5)

        assertEquals(5, emotion.anger)
    }

    @Test
    fun testSadnessEmotionChange(){
        val emotion = EmotionState()
        emotion.updateEmotion(Emotions.SADNESS, 5)

        assertEquals(5, emotion.sadness)
    }

    @Test
    fun testIsReadyForPicking() {

        val arm = Arm(Sides.LEFT)
        val jaw = Jaw()
        val head = Head("ds")
        val teeth = Teeth()

        val method: Method = Arm::class.java.getDeclaredMethod("isReadyForPicking", Jaw::class.java, Head::class.java, Teeth::class.java)
        method.isAccessible = true


        jaw.isBusy = false
        head.isBusy = false
        teeth.isBeingPicked = false
        assertTrue(method.invoke(arm, jaw, head, teeth) as Boolean)


        jaw.isBusy = true
        assertFalse(method.invoke(arm, jaw, head, teeth) as Boolean)


        jaw.isBusy = false
        head.isBusy = true
        assertFalse(method.invoke(arm, jaw, head, teeth) as Boolean)


        head.isBusy = false
        teeth.isBeingPicked = true
        assertFalse(method.invoke(arm, jaw, head, teeth) as Boolean)

        teeth.isBeingPicked = false
        arm.isBusy = true
        assertFalse(method.invoke(arm, jaw, head, teeth) as Boolean)
    }

}