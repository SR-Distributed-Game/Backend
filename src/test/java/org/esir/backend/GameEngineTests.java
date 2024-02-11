package org.esir.backend;

import GameEngineTestRessources.TestGameObject;
import org.esir.backend.GameEngine.Game;
import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.Scene;
import org.esir.backend.GameEngine.Transform;
import org.esir.backend.IOFormat.JSONFormat;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class GameEngineTests {

    Scene scene;

    public GameEngineTests(){
        scene = new Scene();
        Game.getInstance().setScene(scene);
    }


    @Test
    public void testTransformGetX() {
        Transform t = new Transform();
        assertEquals(t.getPosition().getX(), 0);
    }

    @Test
    public void testSceneAddGameObject() {
        scene.addGameObject(new GameObject());
        assertEquals(scene.getGameObjects().size(), 1);
    }

    @Test
    public void testSceneRemoveGameObject() {
        GameObject go = new GameObject();
        scene.addGameObject(go);
        scene.removeGameObject(go.getId());
        assertEquals(scene.getGameObjects().size(), 0);
    }

    @Test
    public void testSceneGetGameObject() {
        GameObject go = new GameObject();
        scene.addGameObject(go);
        assertEquals(scene.getGameObject(go.getId()), go);
    }

    @Test
    public void testSerializing() {
        GameObject go = new GameObject();
        go.setName("test");
        go.setId(0);
        go.getTransform().getPosition().setX(1);
        go.getTransform().getPosition().setY(2);
        go.getTransform().getScale().setX(3);
        go.getTransform().getScale().setY(4);
        go.getTransform().setRotation(5);
        assertEquals(go.toSerialized().toString(), "{\"transform\":{\"rotation\":5,\"scale\":{\"x\":3,\"y\":4},\"position\":{\"x\":1,\"y\":2}},\"Type\":\"GameObject\",\"name\":\"test\",\"id\":0}");
    }

    @Test
    public void testDeserializing() {
        GameObject go = new GameObject();
        go.setName("test");
        go.getTransform().getPosition().setX(1);
        go.getTransform().getPosition().setY(2);
        go.getTransform().getScale().setX(3);
        go.getTransform().getScale().setY(4);
        go.getTransform().setRotation(5);
        GameObject go2 = new GameObject();
        go2.updateFromRequest(go.toSerialized());
        assertEquals(go2.getName(), "test");
        assertEquals(go2.getTransform().getPosition().getX(), 1);
        assertEquals(go2.getTransform().getPosition().getY(), 2);
        assertEquals(go2.getTransform().getScale().getX(), 3);
        assertEquals(go2.getTransform().getScale().getY(), 4);
        assertEquals(go2.getTransform().getRotation(), 5);
    }

    @Test
    public void testFromSerialized() {
        TestGameObject go = new TestGameObject();
        go.setName("test");
        go.setId(0);
        go.getTransform().getPosition().setX(1);
        go.getTransform().getPosition().setY(2);
        go.getTransform().getScale().setX(3);
        go.getTransform().getScale().setY(4);
        go.getTransform().setRotation(5);

        TestGameObject go2 = GameObject.fromSerialized(TestGameObject.class, go.toSerialized());
        assertEquals(go2.getTransform().getPosition().getX(), 1);
        assertEquals(go2.getTransform().getPosition().getY(), 2);
        assertEquals(go2.getTransform().getScale().getX(), 3);
        assertEquals(go2.getTransform().getScale().getY(), 4);
        assertEquals(go2.getTransform().getRotation(), 5);
    }

    @Test
    public void testNewIdGreaterThanPrevious() {
        GameObject go = new GameObject();
        GameObject go2 = new GameObject();
        go.AcquireNewId();
        go2.AcquireNewId();
        assertEquals(go.getId() + 1, go2.getId());
    }




}