package org.esir.backend.GameEngine;

import org.esir.backend.GameEngine.Components.ColliderComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SpatialHashmap extends CollisionSystem{
    private final int cellSize;
    private final Map<Integer, List<ColliderComponent>> map;
    private final Map<String, Boolean> alreadyCollided;
    private final Map<ColliderComponent, List<Integer>> cellMap;


    public SpatialHashmap(int cellSize) {
        this.cellSize = cellSize;
        this.map = new HashMap<>();
        this.alreadyCollided = new HashMap<>();
        this.cellMap = new HashMap<>();
    }



    public void insert(ColliderComponent collider) {
        List<Integer> cells = getCells(collider);
        cells.forEach(cell -> {
            map.computeIfAbsent(cell, k -> new ArrayList<>()).add(collider);
        });
    }

    public void remove(ColliderComponent collider) {
        List<Integer> cells = getCells(collider);
        cells.forEach(cell -> {
            List<ColliderComponent> list = map.get(cell);
            if (list != null) {
                list.remove(collider);
            }
        });
    }

    public void clear() {
        map.clear();
        cellMap.clear();
    }

    private List<Integer> getCells(ColliderComponent collider) {
        var transform = collider.getParent().getTransform();
        int x = (int) Math.floor(transform.getPosition().getX() / cellSize);
        int y = (int) Math.floor(transform.getPosition().getY() / cellSize);
        int x2 = (int) Math.floor((transform.getPosition().getX() + transform.getScale().getX()) / cellSize);
        int y2 = (int) Math.floor((transform.getPosition().getY() + transform.getScale().getY()) / cellSize);

        Set<Integer> cellSet = new HashSet<>();
        for (int i = x; i <= x2; i++) {
            for (int j = y; j <= y2; j++) {
                cellSet.add(getCellHash(i, j));
            }
        }
        List<Integer> cells = new ArrayList<>(cellSet);
        cellMap.put(collider, cells);
        return cells;
    }

    private int getCellHash(int x, int y) {
        return x * 31393 + y * 6353;
    }

    public boolean checkAABBCollision(ColliderComponent collider1, ColliderComponent collider2) {
        // Assuming ColliderComponent or its parent GameObject has methods to get position and size
        var transform1 = collider1.getParent().getTransform();
        var transform2 = collider2.getParent().getTransform();
        return transform2.intersects(transform1);
    }

    public Set<ColliderComponent> query(ColliderComponent collider) {
        Set<ColliderComponent> result = new HashSet<>();
        List<Integer> cells = getCells(collider);
        for (Integer cell : cells) {
            List<ColliderComponent> collidersInCell = map.get(cell);
            if (collidersInCell != null) {
                result.addAll(collidersInCell);
            }
        }
        return result;
    }

    public void addCollisionHash(ColliderComponent collider1, ColliderComponent collider2) {
        // Create a unique key for the collider pair. The order does not matter,
        // so we ensure consistency by comparing IDs (assuming getId() returns a unique identifier).
        int id1 = collider1.getParent().getId();
        int id2 = collider2.getParent().getId();
        String key = (id1+"").compareTo((id2+"")) < 0 ? id1 + "_" + id2 : id2 + "_" + id1;

        // Record the collision in the alreadyCollided map
        alreadyCollided.put(key, Boolean.TRUE);
    }

    public boolean checkCollisionHash(ColliderComponent collider1, ColliderComponent collider2) {
        // Generate a consistent key for the pair of colliders, independent of their order
        int id1 = collider1.getParent().getId();
        int id2 = collider2.getParent().getId();
        String key = (id1+"").compareTo((id2+"")) < 0 ? id1 + "_" + id2 : id2 + "_" + id1;

        // Check if this pair has already been marked as collided
        return alreadyCollided.containsKey(key);
    }

    public void checkCollisions(ColliderComponent collider) {
        Set<ColliderComponent> possibleCollisions = query(collider);
        for (ColliderComponent otherCollider : possibleCollisions) {
            if (otherCollider == collider || checkCollisionHash(collider, otherCollider)) {
                continue;
            }
            addCollisionHash(collider, otherCollider);
            if (checkAABBCollision(collider, otherCollider)) {
                collider.onCollisionEnter(otherCollider);
                otherCollider.onCollisionEnter(collider);
            }
        }
    }

    public void update() {
        for (Map.Entry<Integer, List<ColliderComponent>> entry : map.entrySet()) {
            List<ColliderComponent> colliders = entry.getValue();
            for ( ColliderComponent coll : colliders) {
                checkCollisions(coll);
            }
        }
        alreadyCollided.clear();
    }

}
