package org.esir.backend;

import org.esir.backend.GameEngine.Game;
import org.esir.backend.GameEngine.GameObject;
import org.esir.backend.GameEngine.Scene;
import org.esir.backend.GameEngine.Transform;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


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
        assert t.getPosition().getX() == 0;
    }

    @Test
    public void testSceneAddGameObject() {
        scene.addGameObject(new GameObject());
        assert scene.getGameObjects().size() == 1;
    }

    @Test
    public void testSceneRemoveGameObject() {
        GameObject go = new GameObject();
        scene.addGameObject(go);
        scene.removeGameObject(go.getId());
        assert scene.getGameObjects().size() == 0;
    }

    @Test
    public void testSceneGetGameObject() {
        GameObject go = new GameObject();
        scene.addGameObject(go);
        assert scene.getGameObject(go.getId()) == go;
    }

    @Test
    public void testSerializing() {
        GameObject go = new GameObject();
        go.setName("test");
        go.getTransform().getPosition().setX(1);
        go.getTransform().getPosition().setY(2);
        go.getTransform().getScale().setX(3);
        go.getTransform().getScale().setY(4);
        go.getTransform().setRotation(5);
        System.out.println(go.toSerialized().toString());
        assert go.toSerialized().toString().equals("{\"transform\":{\"rotation\":5,\"scale\":{\"x\":3,\"y\":4},\"position\":{\"x\":1,\"y\":2}},\"Type\":\"GameObject\",\"name\":\"test\",\"id\":2}");
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
        System.out.println(go2.toSerialized().toString());
        assert go2.getName().equals("test");
        assert go2.getTransform().getPosition().getX() == 1;
        assert go2.getTransform().getPosition().getY() == 2;
        assert go2.getTransform().getScale().getX() == 3;
        assert go2.getTransform().getScale().getY() == 4;
        assert go2.getTransform().getRotation() == 5;
    }




}