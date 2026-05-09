from PIL import Image, ImageDraw, ImageFont
import math
from pathlib import Path


ROOT = Path(__file__).resolve().parent
FONT_PATHS = [
    Path("C:/Windows/Fonts/arial.ttf"),
    Path("C:/Windows/Fonts/segoeui.ttf"),
]


def font(size, bold=False):
    candidates = [
        Path("C:/Windows/Fonts/arialbd.ttf") if bold else Path("C:/Windows/Fonts/arial.ttf"),
        Path("C:/Windows/Fonts/segoeuib.ttf") if bold else Path("C:/Windows/Fonts/segoeui.ttf"),
    ]
    for candidate in candidates:
        if candidate.exists():
            return ImageFont.truetype(str(candidate), size)
    return ImageFont.load_default()


TITLE = font(36, True)
HEADER = font(20, True)
BODY = font(17)
SMALL = font(14)


def text_size(draw, text, used_font):
    box = draw.textbbox((0, 0), text, font=used_font)
    return box[2] - box[0], box[3] - box[1]


def class_box(draw, xy, width, title, stereotype, attributes, methods, fill):
    x, y = xy
    padding = 12
    line_height = 24
    header_height = 58 if stereotype else 44
    height = header_height + padding
    height += max(1, len(attributes)) * line_height + padding
    height += max(1, len(methods)) * line_height + padding

    draw.rounded_rectangle((x, y, x + width, y + height), radius=8, fill=fill, outline="#263238", width=2)
    draw.rectangle((x, y, x + width, y + header_height), fill=fill, outline="#263238", width=2)

    if stereotype:
        sw, _ = text_size(draw, stereotype, SMALL)
        draw.text((x + (width - sw) / 2, y + 8), stereotype, fill="#263238", font=SMALL)
        tw, _ = text_size(draw, title, HEADER)
        draw.text((x + (width - tw) / 2, y + 28), title, fill="#111111", font=HEADER)
    else:
        tw, _ = text_size(draw, title, HEADER)
        draw.text((x + (width - tw) / 2, y + 13), title, fill="#111111", font=HEADER)

    current_y = y + header_height + padding / 2
    draw.line((x, current_y - 6, x + width, current_y - 6), fill="#263238", width=1)
    if attributes:
        for item in attributes:
            draw.text((x + padding, current_y), item, fill="#111111", font=BODY)
            current_y += line_height
    else:
        draw.text((x + padding, current_y), " ", fill="#111111", font=BODY)
        current_y += line_height

    current_y += padding / 2
    draw.line((x, current_y - 6, x + width, current_y - 6), fill="#263238", width=1)
    if methods:
        for item in methods:
            draw.text((x + padding, current_y), item, fill="#111111", font=BODY)
            current_y += line_height
    else:
        draw.text((x + padding, current_y), " ", fill="#111111", font=BODY)
        current_y += line_height

    return (x, y, x + width, y + height)


def center(box):
    x1, y1, x2, y2 = box
    return ((x1 + x2) / 2, (y1 + y2) / 2)


def edge_point(box, side):
    x1, y1, x2, y2 = box
    if side == "top":
        return ((x1 + x2) / 2, y1)
    if side == "bottom":
        return ((x1 + x2) / 2, y2)
    if side == "left":
        return (x1, (y1 + y2) / 2)
    return (x2, (y1 + y2) / 2)


def draw_arrow(draw, start, end, color="#263238", width=2, dashed=False, hollow=False, label=None):
    if dashed:
        draw_dashed_line(draw, start, end, fill=color, width=width)
    else:
        draw.line((start[0], start[1], end[0], end[1]), fill=color, width=width)

    angle = math.atan2(end[1] - start[1], end[0] - start[0])
    size = 14
    left = (end[0] - size * math.cos(angle - math.pi / 6), end[1] - size * math.sin(angle - math.pi / 6))
    right = (end[0] - size * math.cos(angle + math.pi / 6), end[1] - size * math.sin(angle + math.pi / 6))
    if hollow:
        draw.polygon([end, left, right], fill="white", outline=color)
    else:
        draw.polygon([end, left, right], fill=color)

    if label:
        mx = (start[0] + end[0]) / 2
        my = (start[1] + end[1]) / 2
        draw.rectangle((mx - 4, my - 11, mx + 8 * len(label), my + 8), fill="white")
        draw.text((mx, my - 10), label, fill=color, font=SMALL)


def draw_dashed_line(draw, start, end, fill, width):
    x1, y1 = start
    x2, y2 = end
    dx = x2 - x1
    dy = y2 - y1
    length = math.hypot(dx, dy)
    if length == 0:
        return
    dash = 12
    gap = 8
    steps = int(length / (dash + gap)) + 1
    for i in range(steps):
        a = i * (dash + gap) / length
        b = min((i * (dash + gap) + dash) / length, 1)
        draw.line((x1 + dx * a, y1 + dy * a, x1 + dx * b, y1 + dy * b), fill=fill, width=width)


def save_iterator():
    image = Image.new("RGB", (1900, 1180), "#fbfbf8")
    draw = ImageDraw.Draw(image)
    draw.text((60, 35), "Iterator Pattern: QuestLog traversal without exposing List<Quest>", fill="#111111", font=TITLE)

    boxes = {}
    boxes["QuestLog"] = class_box(draw, (70, 135), 450, "QuestLog", "",
                                  ["- quests: List<Quest>"],
                                  ["+ add(quest): void", "+ size(): int", "+ ordered(): QuestIterator",
                                   "+ reverse(): QuestIterator", "+ priorityAtLeast(threshold): QuestIterator",
                                   "+ rewardSorted(): QuestIterator", "~ snapshot(): List<Quest>"],
                                  "#fff3cf")
    boxes["Quest"] = class_box(draw, (70, 650), 390, "Quest", "",
                               ["- title: String", "- priority: QuestPriority", "- rewardGold: int", "- urgent: boolean"],
                               ["+ getTitle(): String", "+ getPriority(): QuestPriority",
                                "+ getRewardGold(): int", "+ isUrgent(): boolean"],
                               "#f3f4f6")
    boxes["QuestPriority"] = class_box(draw, (480, 700), 240, "QuestPriority", "<<enum>>",
                                       ["LOW", "NORMAL", "HIGH", "URGENT"], [], "#f3f4f6")
    boxes["QuestIterator"] = class_box(draw, (1070, 450), 420, "QuestIterator", "<<interface>>",
                                       [], ["+ hasNext(): boolean", "+ next(): Quest"], "#dff0ff")

    names = [
        ("OrderedQuestIterator", 735, 160, "#e8f7e8"),
        ("ReverseQuestIterator", 1300, 160, "#e8f7e8"),
        ("PriorityQuestIterator", 735, 780, "#e8f7e8"),
        ("RewardSortedQuestIterator", 1300, 780, "#e8f7e8"),
    ]
    for name, x, y, fill in names:
        boxes[name] = class_box(draw, (x, y), 500, name, "",
                                ["- snapshot: List<Quest>", "- cursor: int"],
                                ["+ hasNext(): boolean", "+ next(): Quest"], fill)

    draw_arrow(draw, edge_point(boxes["QuestLog"], "right"), edge_point(boxes["QuestIterator"], "left"),
               dashed=True, label="creates")
    draw_arrow(draw, edge_point(boxes["QuestLog"], "bottom"), edge_point(boxes["Quest"], "top"),
               hollow=True, label="owns")
    draw_arrow(draw, edge_point(boxes["Quest"], "right"), edge_point(boxes["QuestPriority"], "left"),
               dashed=True, label="uses")

    for name, _, y, _ in names:
        if y < 450:
            draw_arrow(draw, edge_point(boxes[name], "bottom"), edge_point(boxes["QuestIterator"], "top"),
                       dashed=True, hollow=True)
        else:
            draw_arrow(draw, edge_point(boxes[name], "top"), edge_point(boxes["QuestIterator"], "bottom"),
                       dashed=True, hollow=True)

    draw.rounded_rectangle((760, 1025, 1785, 1095), radius=8, fill="#ffffff", outline="#607d8b", width=2)
    draw.text((785, 1044),
              "All concrete quest iterators are created by QuestLog and traverse private snapshot lists.",
              fill="#263238", font=BODY)

    image.save(ROOT / "iterator_pattern.png")


def save_mediator():
    image = Image.new("RGB", (1900, 1280), "#fbfbf8")
    draw = ImageDraw.Draw(image)
    draw.text((60, 35), "Mediator Pattern: topic routing through GuildHall", fill="#111111", font=TITLE)

    boxes = {}
    boxes["GuildMediator"] = class_box(draw, (90, 125), 460, "GuildMediator", "<<interface>>",
                                       [], ["+ register(member): void", "+ dispatch(topic, from, payload): void"],
                                       "#dff0ff")
    boxes["GuildHall"] = class_box(draw, (720, 105), 610, "GuildHall", "",
                                   ["- membersByTopic: Map<String, List<GuildMember>>",
                                    "- lastDispatchNotificationCount: int",
                                    "- totalMessagesRouted: int", "- totalNotifications: int"],
                                   ["+ register(member): void", "+ dispatch(topic, from, payload): void",
                                    "+ getLastDispatchNotificationCount(): int", "+ resetStatistics(): void"],
                                   "#fff3cf")
    boxes["GuildMember"] = class_box(draw, (610, 430), 560, "GuildMember", "<<abstract>>",
                                     ["- name: String", "- mediator: GuildMediator"],
                                     ["+ getName(): String", "# getMediator(): GuildMediator",
                                      "# senderName(from): String", "+ receive(topic, from, payload): void"],
                                     "#f3f4f6")

    members = [
        ("Quartermaster", 90, 820, "+ requestSupplies(payload): void", "#e8f7e8"),
        ("Scout", 510, 820, "+ reportRoute(payload): void", "#e8f7e8"),
        ("Healer", 930, 820, "+ prepareAid(payload): void", "#e8f7e8"),
        ("Captain", 1350, 820, "+ issueOrder(payload): void", "#e8f7e8"),
        ("Loremaster", 720, 1050, "+ shareLore(payload): void", "#e8f7e8"),
    ]
    for name, x, y, method, fill in members:
        boxes[name] = class_box(draw, (x, y), 390, name, "",
                                [], [method, "+ receive(topic, from, payload): void"], fill)

    draw_arrow(draw, edge_point(boxes["GuildHall"], "left"), edge_point(boxes["GuildMediator"], "right"),
               dashed=True, hollow=True, label="implements")
    draw_arrow(draw, edge_point(boxes["GuildMember"], "left"), edge_point(boxes["GuildMediator"], "bottom"),
               label="mediator")
    draw_arrow(draw, edge_point(boxes["GuildHall"], "bottom"), edge_point(boxes["GuildMember"], "top"),
               hollow=True, label="topic subscribers")

    for name, _, _, _, _ in members:
        draw_arrow(draw, edge_point(boxes[name], "top"), edge_point(boxes["GuildMember"], "bottom"),
                   hollow=True)

    draw.rounded_rectangle((1375, 135, 1790, 315), radius=8, fill="#ffffff", outline="#607d8b", width=2)
    draw.text((1395, 158), "Routing rule", fill="#111111", font=HEADER)
    draw.text((1395, 198), "Concrete guild members do not", fill="#263238", font=BODY)
    draw.text((1395, 222), "store direct colleague references.", fill="#263238", font=BODY)
    draw.text((1395, 258), "Outbound messages go through", fill="#263238", font=BODY)
    draw.text((1395, 282), "getMediator().dispatch(...).", fill="#263238", font=BODY)

    image.save(ROOT / "mediator_pattern.png")


if __name__ == "__main__":
    save_iterator()
    save_mediator()
