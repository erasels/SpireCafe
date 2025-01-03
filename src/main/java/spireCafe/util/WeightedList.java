package spireCafe.util;

import com.megacrit.cardcrawl.random.Random;

import java.util.ArrayList;
import java.util.List;

// This is not an actual list, be warned
public class WeightedList<T> {
    private final List<Item> items;
    private int totalWeight;

    public WeightedList() {
        totalWeight = 0;
        items = new ArrayList<>();
    }

    public int size() {
        return items.size();
    }

    public void add(T object, int weight) {
        totalWeight += weight;
        items.add(new Item(object, weight));
    }

    public void addAll(List<T> objects, int weight) {
        totalWeight += (weight*objects.size());
        objects.forEach(o -> items.add(new Item(o, weight)));
    }

    public T getRandom(Random rng) {
        return getRandom(rng, false);
    }

    public T getRandom(Random rng, boolean remove) {
        int r = rng.random(totalWeight);
        int currentWeight = 0;

        Item selected = null;
        for (Item item : items) {
            if ((currentWeight + item.weight) >= r) {
                selected = item;

                break;
            }
            currentWeight += item.weight;
        }

        if (selected != null) {
            if (remove) {
                remove(selected);
            }

            return selected.object;
        } else {
            return null;
        }
    }

    private void remove(Item item) {
        totalWeight -= item.weight;
        items.remove(item);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    private class Item {
        final int weight;
        final T object;

        private Item(T object, int weight) {
            this.weight = weight;
            this.object = object;
        }
    }
}
